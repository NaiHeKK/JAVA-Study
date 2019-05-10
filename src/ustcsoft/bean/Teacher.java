package ustcsoft.bean;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

public class Teacher {
	private String teacherID;// 工号
	private String teacherName;// 名字
	private int time;// 入职时长
	private int age;// 年纪
	private BigDecimal pay;// 工资
	private Date startTime;// 入职时间
	private String text;// 备考

	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return "学号：" + this.getTeacherID() + "  " + "姓名：" + this.getTeacherName() + "  " + "年龄：" + this.getAge() + "  "
				+ "入学时间：" + this.getStartTime() + "  " + "备考：" + this.getText() + "  " + "工资：" + this.getPay() + "  "
				+ "入职时长：" + this.getTime();
	}

	public String getTeacherID() {
		return teacherID;
	}

	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public int getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public int getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public BigDecimal getPay() {
		return pay;
	}

	public void setPay(BigDecimal pay) {
		this.pay = pay;
	}

	public String getStartTime() {
		return DateFormat.getDateInstance(DateFormat.FULL).format(startTime);// 格式化时间
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
