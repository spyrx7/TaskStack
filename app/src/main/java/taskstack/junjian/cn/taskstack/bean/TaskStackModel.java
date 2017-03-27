package taskstack.junjian.cn.taskstack.bean;

import java.io.Serializable;

/**
 * Created by junjianliu
 * on 17/3/7
 * email:spyhanfeng@qq.com
 */
public class TaskStackModel implements Serializable {

    private String id;
    private String name;
    private String startdatetime;
    private String enddatetime;
    private boolean isStack;
    private String content;
    private String tagColor;
    private int status;
    private String updatetime;
    private String userid;

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartdatetime() {
        return startdatetime;
    }

    public void setStartdatetime(String startdatetime) {
        this.startdatetime = startdatetime;
    }

    public String getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(String enddatetime) {
        this.enddatetime = enddatetime;
    }

    public boolean isStack() {
        return isStack;
    }

    public void setStack(boolean stack) {
        isStack = stack;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskStackModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", startdatetime='" + startdatetime + '\'' +
                ", enddatetime='" + enddatetime + '\'' +
                ", isStack=" + isStack +
                ", content='" + content + '\'' +
                ", status=" + status +
                '}';
    }
}
