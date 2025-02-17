package com.hummerrisk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hummer.quartz.service.QuartzManageService;
import com.hummerrisk.base.domain.*;
import com.hummerrisk.base.mapper.*;
import com.hummerrisk.base.mapper.ext.ExtCloudTaskMapper;
import com.hummerrisk.commons.constants.CloudTaskConstants;
import com.hummerrisk.commons.constants.ResourceOperation;
import com.hummerrisk.commons.constants.ResourceTypeConstants;
import com.hummerrisk.commons.constants.ScanTypeConstants;
import com.hummerrisk.commons.exception.HRException;
import com.hummerrisk.commons.utils.*;
import com.hummerrisk.dto.CloudTaskCopyDTO;
import com.hummerrisk.dto.CloudTaskDTO;
import com.hummerrisk.dto.CloudTaskItemLogDTO;
import com.hummerrisk.dto.QuartzTaskDTO;
import com.hummerrisk.i18n.Translator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author harris
 */
@Service
public class OrderService {

    @Resource @Lazy
    private CloudTaskMapper cloudTaskMapper;
    @Resource @Lazy
    private CloudTaskItemMapper cloudTaskItemMapper;
    @Resource @Lazy
    private ExtCloudTaskMapper extCloudTaskMapper;
    @Resource @Lazy
    private CloudTaskItemLogMapper cloudTaskItemLogMapper;
    @Resource @Lazy
    private CloudResourceItemMapper cloudResourceItemMapper;
    @Resource @Lazy
    private CommonThreadPool commonThreadPool;
    @Resource @Lazy
    private CloudTaskItemResourceMapper cloudTaskItemResourceMapper;
    @Resource @Lazy
    private ResourceMapper resourceMapper;
    @Resource @Lazy
    private AccountMapper accountMapper;
    @Resource @Lazy
    private QuartzManageService quartzManageService;
    @Resource @Lazy
    private RuleMapper ruleMapper;
    @Resource @Lazy
    private ResourceRuleMapper resourceRuleMapper;
    @Resource @Lazy
    private RuleTagMappingMapper ruleTagMappingMapper;
    @Resource @Lazy
    private NoticeService noticeService;
    @Resource @Lazy
    private CloudAccountQuartzTaskMapper quartzTaskMapper;
    @Resource @Lazy
    private CloudAccountQuartzTaskRelationMapper quartzTaskRelationMapper;
    @Resource @Lazy
    private CloudAccountQuartzTaskRelaLogMapper quartzTaskRelaLogMapper;
    @Resource @Lazy
    private HistoryService historyService;
    @Resource @Lazy
    private HistoryCloudTaskMapper historyCloudTaskMapper;

    public CloudTask createTask(QuartzTaskDTO quartzTaskDTO, String status, String messageOrderId) throws Exception {
        CloudTask cloudTask = createTaskOrder(quartzTaskDTO, status, messageOrderId);
        String taskId = cloudTask.getId();

        String script = quartzTaskDTO.getScript();
        JSONArray jsonArray = JSON.parseArray(quartzTaskDTO.getParameter());
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            String key = "${{" + jsonObject.getString("key") + "}}";
            if (script.contains(key)) {
                script = script.replace(key, jsonObject.getString("defaultValue"));
            }
        }

        this.deleteTaskItems(taskId);
        List<String> resourceTypes = new ArrayList();
        for (SelectTag selectTag : quartzTaskDTO.getSelectTags()) {
            for (String regionId : selectTag.getRegions()) {
                CloudTaskItemWithBLOBs taskItemWithBLOBs = new CloudTaskItemWithBLOBs();
                String uuid = UUIDUtil.newUUID();
                taskItemWithBLOBs.setId(uuid);
                taskItemWithBLOBs.setTaskId(taskId);
                taskItemWithBLOBs.setRuleId(quartzTaskDTO.getId());
                taskItemWithBLOBs.setCustomData(script);
                taskItemWithBLOBs.setStatus(CloudTaskConstants.TASK_STATUS.UNCHECKED.name());
                taskItemWithBLOBs.setSeverity(quartzTaskDTO.getSeverity());
                taskItemWithBLOBs.setCreateTime(cloudTask.getCreateTime());
                taskItemWithBLOBs.setAccountId(selectTag.getAccountId());
                AccountWithBLOBs account = accountMapper.selectByPrimaryKey(selectTag.getAccountId());
                taskItemWithBLOBs.setAccountUrl(account.getPluginIcon());
                taskItemWithBLOBs.setAccountLabel(account.getName());
                taskItemWithBLOBs.setRegionId(regionId);
                taskItemWithBLOBs.setRegionName(PlatformUtils.tranforRegionId2RegionName(regionId, cloudTask.getPluginId()));
                taskItemWithBLOBs.setTags(cloudTask.getRuleTags());
                cloudTaskItemMapper.insertSelective(taskItemWithBLOBs);

                historyService.insertHistoryCloudTaskItem(BeanUtils.copyBean(new HistoryCloudTaskItemWithBLOBs(), taskItemWithBLOBs));//插入历史数据

                final String finalScript = script;
                commonThreadPool.addTask(() -> {
                    String sc = "";
                    String dirPath = "";
                    try {
                        dirPath = CommandUtils.saveAsFile(finalScript, CloudTaskConstants.RESULT_FILE_PATH_PREFIX + taskId + "/" + regionId, "policy.yml", false);
                    } catch (Exception e) {
                        LogUtil.error("[{}] Generate policy.yml file，and custodian run failed:{}", taskId + "/" + regionId, e.getMessage());
                    }
                    Yaml yaml = new Yaml();
                    Map map = null;
                    try {
                        map = (Map) yaml.load(new FileInputStream(dirPath + "/policy.yml"));
                    } catch (FileNotFoundException e) {
                        LogUtil.error(e.getMessage());
                    }
                    if (map != null) {
                        List<Map> list = (List) map.get("policies");
                        for (Map m : list) {
                            String dirName = m.get("name").toString();
                            String resourceType = m.get("resource").toString();

                            if (!PlatformUtils.checkAvailableRegion(account.getPluginId(), resourceType, regionId)) {
                                continue;
                            }

                            CloudTaskItemResourceWithBLOBs taskItemResource = new CloudTaskItemResourceWithBLOBs();
                            taskItemResource.setTaskId(taskId);
                            taskItemResource.setTaskItemId(taskItemWithBLOBs.getId());
                            taskItemResource.setDirName(dirName);
                            taskItemResource.setResourceType(resourceType);
                            taskItemResource.setResourceName(dirName);

                            //包含actions
                            Map<String, Object> paramMap = new HashMap<>();
                            paramMap.put("policies", Collections.singletonList(m));
                            taskItemResource.setResourceCommandAction(yaml.dump(paramMap));

                            //不包含actions
                            m.remove("actions");
                            paramMap.put("policies", Collections.singletonList(m));
                            taskItemResource.setResourceCommand(yaml.dump(paramMap));
                            cloudTaskItemResourceMapper.insertSelective(taskItemResource);

                            try {
                                HistoryCloudTaskResourceWithBLOBs historyCloudTaskResourceWithBLOBs = new HistoryCloudTaskResourceWithBLOBs();
                                BeanUtils.copyBean(historyCloudTaskResourceWithBLOBs, taskItemResource);
                                historyService.insertHistoryCloudTaskResource(historyCloudTaskResourceWithBLOBs);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            resourceTypes.add(resourceType);
                        }

                        map.put("policies", list);
                        sc = yaml.dump(map);
                        taskItemWithBLOBs.setDetails(sc);
                        cloudTaskItemMapper.updateByPrimaryKeySelective(taskItemWithBLOBs);

                        try {
                            historyService.updateHistoryCloudTaskItem(BeanUtils.copyBean(new HistoryCloudTaskItemWithBLOBs(), taskItemWithBLOBs));//插入历史数据
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        CloudResourceItemExample cloudResourceItemExample = new CloudResourceItemExample();
                        cloudResourceItemExample.createCriteria().andAccountIdEqualTo(quartzTaskDTO.getAccountId()).andResourceTypeIn(resourceTypes);
                        long resourceSum = cloudResourceItemMapper.countByExample(cloudResourceItemExample);
                        cloudTask.setResourcesSum(resourceSum);
                        cloudTask.setResourceTypes(new HashSet<>(resourceTypes).toString());
                        cloudTaskMapper.updateByPrimaryKeySelective(cloudTask);

                        try {
                            historyService.updateHistoryCloudTask(BeanUtils.copyBean(new HistoryCloudTask(), cloudTask));//插入历史数据
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
        //向首页活动添加操作信息
        OperationLogService.log(SessionUtils.getUser(), taskId, cloudTask.getTaskName(), ResourceTypeConstants.TASK.name(), ResourceOperation.SCAN, "i18n_create_scan_task");
        return cloudTask;
    }

    private void deleteTaskItems (String taskId) {
        CloudTaskItemExample cloudTaskItemExample = new CloudTaskItemExample();
        cloudTaskItemExample.createCriteria().andTaskIdEqualTo(taskId);
        List<CloudTaskItem> cloudTaskItems = cloudTaskItemMapper.selectByExample(cloudTaskItemExample);

        for (CloudTaskItem cloudTaskItem : cloudTaskItems) {
            CloudTaskItemLogExample cloudTaskItemLogExample = new CloudTaskItemLogExample();
            cloudTaskItemLogExample.createCriteria().andTaskItemIdEqualTo(cloudTaskItem.getId());
            cloudTaskItemLogMapper.deleteByExample(cloudTaskItemLogExample);

            CloudTaskItemResourceExample cloudTaskItemResourceExample = new CloudTaskItemResourceExample();
            cloudTaskItemResourceExample.createCriteria().andTaskItemIdEqualTo(cloudTaskItem.getId());
            List<CloudTaskItemResource> cloudTaskItemResources = cloudTaskItemResourceMapper.selectByExample(cloudTaskItemResourceExample);
            for (CloudTaskItemResource cloudTaskItemResource : cloudTaskItemResources) {
                resourceMapper.deleteByPrimaryKey(cloudTaskItemResource.getResourceId());
                resourceRuleMapper.deleteByPrimaryKey(cloudTaskItemResource.getResourceId());
            }
            cloudTaskItemResourceMapper.deleteByExample(cloudTaskItemResourceExample);
        }
        cloudTaskItemMapper.deleteByExample(cloudTaskItemExample);
    }

    private CloudTask createTaskOrder(QuartzTaskDTO quartzTaskDTO, String status, String messageOrderId) throws Exception {
        CloudTask cloudTask = new CloudTask();
        cloudTask.setTaskName(quartzTaskDTO.getTaskName() != null ?quartzTaskDTO.getTaskName():quartzTaskDTO.getName());
        cloudTask.setRuleId(quartzTaskDTO.getId());
        cloudTask.setSeverity(quartzTaskDTO.getSeverity());
        cloudTask.setType(quartzTaskDTO.getType());
        cloudTask.setPluginId(quartzTaskDTO.getPluginId());
        cloudTask.setPluginIcon(quartzTaskDTO.getPluginIcon());
        cloudTask.setPluginName(quartzTaskDTO.getPluginName());
        cloudTask.setRuleTags(quartzTaskDTO.getTags().toString());
        cloudTask.setDescription(quartzTaskDTO.getDescription());
        cloudTask.setAccountId(quartzTaskDTO.getAccountId());
        cloudTask.setApplyUser(Objects.requireNonNull(SessionUtils.getUser()).getId());
        cloudTask.setStatus(status);
        cloudTask.setScanType(ScanTypeConstants.custodian.name());
        if (quartzTaskDTO.getCron() != null){
            cloudTask.setCron(quartzTaskDTO.getCron());
            cloudTask.setCronDesc(DescCornUtils.descCorn(quartzTaskDTO.getCron()));
        }

        CloudTaskExample example = new CloudTaskExample();
        CloudTaskExample.Criteria criteria = example.createCriteria();
        criteria.andAccountIdEqualTo(quartzTaskDTO.getAccountId()).andTaskNameEqualTo(quartzTaskDTO.getTaskName());
        List<CloudTask> queryCloudTasks = cloudTaskMapper.selectByExample(example);
        if (!queryCloudTasks.isEmpty()) {
            cloudTask.setId(queryCloudTasks.get(0).getId());
            cloudTask.setCreateTime(System.currentTimeMillis());
            cloudTaskMapper.updateByPrimaryKeySelective(cloudTask);

            HistoryCloudTask historyCloudTask = historyCloudTaskMapper.selectByPrimaryKey(queryCloudTasks.get(0).getId());
            if (historyCloudTask != null) {
                historyService.updateHistoryCloudTask(BeanUtils.copyBean(new HistoryCloudTask(), cloudTask));//插入历史数据
            } else {
                historyService.insertHistoryCloudTask(BeanUtils.copyBean(new HistoryCloudTask(), cloudTask));//插入历史数据
            }

        } else {
            String taskId = IDGenerator.newBusinessId(CloudTaskConstants.TASK_ID_PREFIX, SessionUtils.getUser().getId());
            cloudTask.setId(taskId);
            cloudTask.setCreateTime(System.currentTimeMillis());
            cloudTaskMapper.insertSelective(cloudTask);

            historyService.insertHistoryCloudTask(BeanUtils.copyBean(new HistoryCloudTask(), cloudTask));//插入历史数据
        }

        if (StringUtils.isNotEmpty(messageOrderId)) {
            noticeService.createMessageOrderItem(messageOrderId, cloudTask);
        }

        return cloudTask;
    }

    public CloudTaskDTO getTaskDetail(String taskId) {
        return extCloudTaskMapper.getTaskDetail(taskId);
    }

    public CloudTaskDTO getTaskExtendInfo(String taskId) {
        return extCloudTaskMapper.getTaskExtendInfo(taskId);
    }

    void saveTaskItemLog(String taskItemId, String resourcePrimaryKey, String operation, String output, boolean success, String historyType) throws Exception {
        CloudTaskItemLogWithBLOBs cloudTaskItemLog = new CloudTaskItemLogWithBLOBs();
        String operator = "system";
        try {
            if (SessionUtils.getUser() != null) {
                operator = SessionUtils.getUser().getId();
            }
        } catch (Exception e) {
            //防止单元测试无session
        }
        cloudTaskItemLog.setOperator(operator);
        cloudTaskItemLog.setCreateTime(System.currentTimeMillis());
        cloudTaskItemLog.setTaskItemId(StringUtils.isBlank(taskItemId) ? StringUtils.EMPTY : taskItemId);
        cloudTaskItemLog.setResourceId(resourcePrimaryKey);
        cloudTaskItemLog.setOperation(operation);
        cloudTaskItemLog.setOutput(output);
        cloudTaskItemLog.setResult(success);
        cloudTaskItemLogMapper.insertSelective(cloudTaskItemLog);

        if (StringUtils.equalsIgnoreCase(historyType, CloudTaskConstants.HISTORY_TYPE.Cloud.name())) {
            historyService.insertHistoryCloudTaskLog(BeanUtils.copyBean(new HistoryCloudTaskLogWithBLOBs(), cloudTaskItemLog));
        } else if (StringUtils.equalsIgnoreCase(historyType, CloudTaskConstants.HISTORY_TYPE.Vuln.name())) {
            historyService.insertHistoryVulnTaskLog(BeanUtils.copyBean(new HistoryVulnTaskLogWithBLOBs(), cloudTaskItemLog));
        }

    }

    int updateTaskStatus(String taskId, String oldStatus, String newStatus) {
        CloudTaskExample example = new CloudTaskExample();
        CloudTaskExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(taskId);
        if (StringUtils.isNotBlank(oldStatus)) {
            criteria.andStatusEqualTo(oldStatus);
        }
        CloudTask cloudTask = new CloudTask();
        cloudTask.setStatus(newStatus);
        return cloudTaskMapper.updateByExampleSelective(cloudTask, example);
    }

    void updateTaskItemStatus(String taskItemId, CloudTaskConstants.TASK_STATUS status) {
        CloudTaskItemWithBLOBs taskItem = new CloudTaskItemWithBLOBs();
        taskItem.setId(taskItemId);
        taskItem.setStatus(status.name());
        cloudTaskItemMapper.updateByPrimaryKeySelective(taskItem);
    }

    public void updateTaskStatus(String id, String status) {
        CloudTask cloudTask = new CloudTask();
        cloudTask.setId(id);
        cloudTask.setStatus(status);
        cloudTaskMapper.updateByPrimaryKeySelective(cloudTask);
    }

    public List<CloudTask> listAll() {
        return cloudTaskMapper.selectByExample(null);
    }

    public List<CloudTaskItemLogDTO> getQuartzLogByTask(String taskId) {
        List<CloudTaskItemLogDTO> result = new ArrayList<>();
        try {
            CloudTaskItemExample cloudTaskItemExample = new CloudTaskItemExample();
            cloudTaskItemExample.createCriteria().andTaskIdEqualTo(taskId);
            List<CloudTaskItemWithBLOBs> taskItems = cloudTaskItemMapper.selectByExampleWithBLOBs(cloudTaskItemExample);
            for (CloudTaskItemWithBLOBs taskItem : taskItems) {
                CloudTaskItemLogDTO cloudTaskItemLogDTO = new CloudTaskItemLogDTO();
                taskItem.setDetails(null);
                taskItem.setCustomData(null);
                cloudTaskItemLogDTO.setCloudTaskItem(taskItem);
                Rule rule = ruleMapper.selectByPrimaryKey(taskItem.getRuleId());
                rule.setScript(null);//没有用到暂时置空，以防止翻译总报错warn
                cloudTaskItemLogDTO.setRule(rule);
                result.add(cloudTaskItemLogDTO);
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
        }

        return result;
    }

    public CloudTaskItemWithBLOBs taskItemWithBLOBs(String taskItemId) {
        return cloudTaskItemMapper.selectByPrimaryKey(taskItemId);
    }

    public List<CloudTaskItemLogWithBLOBs> getQuartzLogByTaskItemId(CloudTaskItemWithBLOBs taskItemWithBLOBs) {
        CloudTaskItemLogExample cloudTaskItemLogExample = new CloudTaskItemLogExample();
        cloudTaskItemLogExample.createCriteria().andTaskItemIdEqualTo(taskItemWithBLOBs.getId());
        cloudTaskItemLogExample.setOrderByClause("create_time desc");
        return cloudTaskItemLogMapper.selectByExampleWithBLOBs(cloudTaskItemLogExample);
    }

    public List<CloudAccountQuartzTaskRelaLogWithBLOBs> getQuartzLogsById(String qzTaskId) {
        CloudAccountQuartzTaskRelaLogExample example = new CloudAccountQuartzTaskRelaLogExample();
        example.createCriteria().andQuartzTaskIdEqualTo(qzTaskId);
        example.setOrderByClause("create_time desc");
        return quartzTaskRelaLogMapper.selectByExampleWithBLOBs(example);
    }

    public List<CloudTaskItemLogDTO> getTaskItemLogByTaskId(String taskId) {
        List<CloudTaskItemLogDTO> result = new ArrayList<>();
        try {
            CloudTaskItemExample cloudTaskItemExample = new CloudTaskItemExample();
            cloudTaskItemExample.createCriteria().andTaskIdEqualTo(taskId);
            List<CloudTaskItem> cloudTaskItems = cloudTaskItemMapper.selectByExample(cloudTaskItemExample);
            for (CloudTaskItem cloudTaskItem : cloudTaskItems) {
                result.addAll(getTaskItemLogByTaskItemId(cloudTaskItem.getId()));
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
        }

        return result;
    }

    private List<CloudTaskItemLogDTO> getTaskItemLogByTaskItemId(String taskItemId) {
        List<CloudTaskItemLogDTO> result = new ArrayList<>();
        try {
            CloudTaskItemWithBLOBs taskItem = cloudTaskItemMapper.selectByPrimaryKey(taskItemId);
            if (taskItem != null) {
                CloudTaskItemLogDTO cloudTaskItemLogDTO = new CloudTaskItemLogDTO();
                taskItem.setDetails(null);
                taskItem.setCustomData(null);
                cloudTaskItemLogDTO.setCloudTaskItem(taskItem);
                Rule rule = ruleMapper.selectByPrimaryKey(taskItem.getRuleId());
                rule.setScript(null);//没有用到暂时置空，以防止翻译总报错warn
                cloudTaskItemLogDTO.setRule(rule);
                CloudTaskItemLogExample cloudTaskItemLogExample = new CloudTaskItemLogExample();
                cloudTaskItemLogExample.createCriteria().andTaskItemIdEqualTo(taskItem.getId());
                cloudTaskItemLogExample.setOrderByClause("create_time");
                cloudTaskItemLogDTO.setCloudTaskItemLogList(cloudTaskItemLogMapper.selectByExampleWithBLOBs(cloudTaskItemLogExample));
                result.add(cloudTaskItemLogDTO);
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
        }
        return result;
    }

    public CloudTaskCopyDTO copy(String taskId) {
        CloudTaskCopyDTO cloudTaskCopyDTO = new CloudTaskCopyDTO();
        CloudTaskItemExample example = new CloudTaskItemExample();
        example.createCriteria().andTaskIdEqualTo(taskId);
        List<CloudTaskItem> cloudTaskItemList = cloudTaskItemMapper.selectByExample(example);
        cloudTaskCopyDTO.setCloudTaskItemList(cloudTaskItemList);
        cloudTaskCopyDTO.setRule(ruleMapper.selectByPrimaryKey(cloudTaskItemList.get(0).getRuleId()));
        RuleTagMappingExample ruleTagMappingExample = new RuleTagMappingExample();
        ruleTagMappingExample.createCriteria().andRuleIdEqualTo(cloudTaskItemList.get(0).getRuleId());
        List<RuleTagMapping> ruleTagMappings = ruleTagMappingMapper.selectByExample(ruleTagMappingExample);
        cloudTaskCopyDTO.setRuleTagMappingList(ruleTagMappings.stream().map(RuleTagMapping::getTagKey).collect(Collectors.toList()));
        Set<String> set = new HashSet<>();
        List<SelectTag> selectTagList = new LinkedList<>();
        cloudTaskItemList.stream().forEach(item -> {
            set.add(item.getAccountId());
        });
        Objects.requireNonNull(set).forEach(str -> {
            SelectTag selectTag = new SelectTag();
            selectTag.setAccountId(str);
            List<String> regions = new LinkedList<>();
            cloudTaskItemList.forEach(taskItem -> {
                if (StringUtils.equals(taskItem.getAccountId(), str)) {
                    regions.add(taskItem.getRegionId());
                }
            });
            selectTag.setRegions(regions);
            selectTagList.add(selectTag);
        });
        cloudTaskCopyDTO.setSelectTags(selectTagList);
        return cloudTaskCopyDTO;
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, rollbackFor = {RuntimeException.class, Exception.class})
    public void retry(String taskId) throws Exception {
        CloudTask cloudTask = cloudTaskMapper.selectByPrimaryKey(taskId);
        if (cloudTask == null) {
            HRException.throwException(Translator.get("i18n_ex_task_not_found") + taskId);
        }
        try {
            CloudTaskItemExample cloudTaskItemExample = new CloudTaskItemExample();
            cloudTaskItemExample.createCriteria().andTaskIdEqualTo(cloudTask.getId());
            List<CloudTaskItemWithBLOBs> items = cloudTaskItemMapper.selectByExampleWithBLOBs(cloudTaskItemExample);
            for (CloudTaskItemWithBLOBs item : items) {
                if ((StringUtils.equals(cloudTask.getType(), CloudTaskConstants.Type.CREATE.toString()))) {
                    HashSet<String> resourceIdSet2BeReleased = new HashSet<>();

                    //先找到异常的资源
                    if (item.getStatus().equals(CloudTaskConstants.TASK_STATUS.ERROR.name())) {
                        CloudTaskItemLogExample cloudTaskItemLogExample = new CloudTaskItemLogExample();
                        cloudTaskItemLogExample.createCriteria().andTaskItemIdEqualTo(item.getId()).andResourceIdIsNotNull().andResultEqualTo(false);
                        List<CloudTaskItemLogWithBLOBs> cloudTaskItemLogs = cloudTaskItemLogMapper.selectByExampleWithBLOBs(cloudTaskItemLogExample);
                        resourceIdSet2BeReleased.addAll(cloudTaskItemLogs.stream().map(CloudTaskItemLogWithBLOBs::getResourceId).collect(Collectors.toList()));
                    }

                    //释放资源
                    for (String resourceId : resourceIdSet2BeReleased) {
                        if (StringUtils.isBlank(resourceId)) {
                            continue;
                        }
                        String newTaskItemId = item.getId() + "-retry";
                        //删除task_item_resource表相关记录，否则任务重试完成后重试之前的资源还会和任务有关联
                        CloudTaskItemResourceExample cloudTaskItemResourceExample = new CloudTaskItemResourceExample();
                        cloudTaskItemResourceExample.createCriteria().andTaskIdEqualTo(cloudTask.getId()).andTaskItemIdEqualTo(item.getId());
                        cloudTaskItemResourceMapper.deleteByExample(cloudTaskItemResourceExample);
                        //清除日志关联
                        CloudTaskItemLogExample cloudTaskItemLogExample = new CloudTaskItemLogExample();
                        cloudTaskItemLogExample.createCriteria().andTaskItemIdEqualTo(item.getId()).andResourceIdEqualTo(resourceId);
                        CloudTaskItemLogWithBLOBs cloudTaskItemLog = new CloudTaskItemLogWithBLOBs();
                        cloudTaskItemLog.setTaskItemId(newTaskItemId);
                        cloudTaskItemLogMapper.updateByExampleSelective(cloudTaskItemLog, cloudTaskItemLogExample);
                    }

                    if (item.getCount() > Optional.ofNullable(this.getResourceByTaskItemId(item.getId())).orElse(new ArrayList<>()).size()) {
                        saveTaskItemLog(item.getId(), null, "i18n_retry_create_resource", "", true, CloudTaskConstants.HISTORY_TYPE.Cloud.name());
                        item.setStatus(CloudTaskConstants.TASK_STATUS.UNCHECKED.name());
                    } else {
                        item.setStatus(CloudTaskConstants.TASK_STATUS.FINISHED.name());
                    }
                } else {
                    if (item.getStatus().equals(CloudTaskConstants.TASK_STATUS.ERROR.name())) {
                        item.setStatus(CloudTaskConstants.TASK_STATUS.UNCHECKED.name());
                    }
                }
                //查询规则，如果规则有变动，按最新变动的规则执行
                /*    Rule rule = ruleMapper.selectByPrimaryKey(item.getRuleId());
                    String script = rule.getScript();
                    JSONArray jsonArray = JSON.parseArray(rule.getParameter());
                    for (Object o : jsonArray) {
                        JSONObject jsonObject = (JSONObject) o;
                        String key = "${{" + jsonObject.getString("key") + "}}";
                        if (script.contains(key)) {
                            script = script.replace(key, jsonObject.getString("defaultValue"));
                        }
                    }
                    if (!StringUtils.equals(script, item.getCustomData())) {
                        List<String> resourceTypes = new ArrayList();
                        final String finalScript = script;
                        commonThreadPool.addTask(() -> {
                            String sc = "";
                            String dirPath = "";
                            try {
                                dirPath = CommandUtils.saveAsFile(finalScript, CloudTaskConstants.RESULT_FILE_PATH_PREFIX + taskId + "/" + item.getRegion(), "policy.yml", false);
                            } catch (Exception e) {
                                LogUtil.error("[{}] Generate policy.yml file，and custodian run failed:{}", taskId + "/" + item.getRegion(), e.getMessage());
                            }
                            Yaml yaml = new Yaml();
                            Map map = null;
                            try {
                                map = (Map) yaml.load(new FileInputStream(dirPath + "/policy.yml"));
                            } catch (FileNotFoundException e) {
                                LogUtil.error(e.getMessage());
                            }
                            if (map != null) {
                                List<Map> list = (List) map.get("policies");
                                for (Map m : list) {
                                    String dirName = m.get("name").toString();
                                    String resourceType = m.get("resource").toString();
                                    CloudTaskItemResourceWithBLOBs taskItemResource = new CloudTaskItemResourceWithBLOBs();
                                    taskItemResource.setTaskId(taskId);
                                    taskItemResource.setTaskItemId(item.getId());
                                    taskItemResource.setDirName(dirName);
                                    taskItemResource.setResourceType(resourceType);
                                    taskItemResource.setResourceName(dirName);

                                    //包含actions
                                    Map<String, Object> paramMap = new HashMap<>();
                                    paramMap.put("policies", Arrays.asList(m));
                                    taskItemResource.setResourceCommandAction(yaml.dump(paramMap));

                                    //不包含actions
                                    m.remove("actions");
                                    paramMap.put("policies", Arrays.asList(m));
                                    taskItemResource.setResourceCommand(yaml.dump(paramMap));
                                    cloudTaskItemResourceMapper.updateByPrimaryKeySelective(taskItemResource);

                                    resourceTypes.add(resourceType);
                                }
                                map.put("policies", list);
                                sc = yaml.dump(map);
                                item.setCustomData(sc);

                            }
                        });
                    }*/

                cloudTaskItemMapper.updateByPrimaryKey(item);
            }

            updateTaskStatus(cloudTask.getId(), cloudTask.getStatus(), CloudTaskConstants.TASK_STATUS.APPROVED.name());
        } catch (Exception e) {
            LogUtil.error("Failed to retry, TaskId: " + cloudTask.getId(), e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, rollbackFor = {RuntimeException.class, Exception.class})
    public void getCronDesc(String taskId) throws Exception {
        CloudTask cloudTask = cloudTaskMapper.selectByPrimaryKey(taskId);
        if (cloudTask == null) {
            HRException.throwException(Translator.get("i18n_ex_task_not_found") + taskId);
        }
        String cronDesc = DescCornUtils.descCorn(cloudTask.getCron());
        cloudTask.setCronDesc(cronDesc);
        cloudTaskMapper.updateByPrimaryKeySelective(cloudTask);
    }

    public List<ResourceWithBLOBs> getResourceByTaskItemId(String taskItemId) {
        List<ResourceWithBLOBs> list = new LinkedList<>();
        CloudTaskItemResourceExample example = new CloudTaskItemResourceExample();
        example.createCriteria().andTaskItemIdEqualTo(taskItemId);
        List<CloudTaskItemResource> cloudTaskItemResources = cloudTaskItemResourceMapper.selectByExample(example);
        if (CollectionUtils.size(cloudTaskItemResources) != 1) {
            return null;
        }
        for (CloudTaskItemResource itemResource : cloudTaskItemResources) {
            ResourceWithBLOBs resourceWithBLOBs = resourceMapper.selectByPrimaryKey(itemResource.getResourceId());
            list.add(resourceWithBLOBs);
        }
        return list;
    }

    public void createQuartzTask(String quartzTaskId) {

        try {
            CloudAccountQuartzTask quartzTask = quartzTaskMapper.selectByPrimaryKey(quartzTaskId);
            if (quartzTask != null) {
                quartzTask.setStatus(CloudTaskConstants.TASK_STATUS.RUNNING.name());
                Trigger trigger = quartzManageService.getTrigger(new TriggerKey(quartzTask.getTriggerId()));
                quartzTask.setLastFireTime(trigger.getNextFireTime().getTime());
                if (trigger.getPreviousFireTime() != null)
                    quartzTask.setPrevFireTime(trigger.getPreviousFireTime().getTime());
                quartzTaskMapper.updateByPrimaryKeySelective(quartzTask);

                CloudAccountQuartzTaskRelationExample example = new CloudAccountQuartzTaskRelationExample();
                example.createCriteria().andQuartzTaskIdEqualTo(quartzTaskId);
                List<CloudAccountQuartzTaskRelation> list = quartzTaskRelationMapper.selectByExampleWithBLOBs(example);
                for (CloudAccountQuartzTaskRelation quartzTaskRelation : list) {
                    JSONArray jsonArray = JSON.parseArray(quartzTaskRelation.getTaskIds());
                    for (Object o : jsonArray) {
                        CloudTask cloudTask = cloudTaskMapper.selectByPrimaryKey(o.toString());
                        cloudTask.setStatus(CloudTaskConstants.TASK_STATUS.APPROVED.name());
                        cloudTask.setLastFireTime(quartzTask.getLastFireTime());
                        cloudTask.setPrevFireTime(quartzTask.getPrevFireTime());
                        cloudTaskMapper.updateByPrimaryKeySelective(cloudTask);
                    }
                    CloudAccountQuartzTaskRelaLogWithBLOBs quartzTaskRelaLog = new CloudAccountQuartzTaskRelaLogWithBLOBs();
                    quartzTaskRelaLog.setCreateTime(System.currentTimeMillis());
                    quartzTaskRelaLog.setQuartzTaskId(quartzTaskId);
                    quartzTaskRelaLog.setQuartzTaskRelaId(quartzTaskRelation.getId());
                    quartzTaskRelaLog.setTaskIds(quartzTaskRelation.getTaskIds());
                    quartzTaskRelaLog.setSourceId(quartzTaskRelation.getSourceId());
                    quartzTaskRelaLog.setQzType(quartzTaskRelation.getQzType());
                    quartzTaskRelaLog.setOperator("System");
                    quartzTaskRelaLog.setOperation("i18n_exec_qrtz_task");
                    quartzTaskRelaLogMapper.insertSelective(quartzTaskRelaLog);
                }

            }
        } catch (Exception e) {
            LogUtil.error(quartzTaskId + "{createQuartzTask}: " + e.getMessage());
            HRException.throwException(quartzTaskId + "{createQuartzTask}: " + e);
        }
    }
}
