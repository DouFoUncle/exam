package com.project.commons.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.commons.mapper.MessageMapper;
import com.project.commons.model.Message;
/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Service
@AllArgsConstructor
public class MessageService extends ServiceImpl<MessageMapper, Message> {

    private MessageMapper messageMapper;

    /**
     * 分页查询信息
     * @param message
     * @param page
     * @param limit
     * @return
     */
    public IPage<Message> getListByPage(Message message, Integer page, Integer limit) {
        // 创建查询条件
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(message.getUserTeacherId() != null, "user_teacher_id", message.getUserTeacherId());
        queryWrapper.like(!StrUtil.isBlankOrUndefined(message.getTitle()), "title", message.getTitle());
        // 未回复
        if("0".equals(message.getReplyFlag())) {
            queryWrapper.isNull("reply_content");
        } else if("1".equals(message.getReplyFlag())) {
            // 已回复
            queryWrapper.isNotNull("reply_content");
        }
        queryWrapper.orderByDesc("create_date", "reply_date");
        // 调用查询
        IPage<Message> pageBean = new Page<>(page, limit);
        pageBean = messageMapper.selectPage(pageBean, queryWrapper);
        if(pageBean != null && pageBean.getRecords() != null && pageBean.getRecords().size() > 0) {
            for (Message record : pageBean.getRecords()) {
                if(StrUtil.isBlankOrUndefined(record.getReplyContent())) {
                    record.setReplyFlag("0");
                } else {
                    record.setReplyFlag("1");
                }
            }
        }
        return pageBean;
    }

    /**
     *  判断删除的信息中是否有已经回复的信息
     * @param ids
     * @return
     */
    public boolean deleteInfo(String ids) {
        // 分割id串
        List<String> split = StrUtil.split(ids, ',', true, true);
        // 查询要删除的信息中是否已经被回复
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", split);
        // 查询信息
        List<Message> messages = messageMapper.selectList(queryWrapper);
        // 循环判断是否已经被回复
        for (Message message : messages) {
            if(!StrUtil.isBlankOrUndefined(message.getReplyContent())) {
                // 已被回复无法删除
                throw new RuntimeException("要删除的信息中包含了已经被回复的留言，无法删除！");
            }
        }
        // 调用删除
        return this.removeBatchByIds(split);
    }
}
