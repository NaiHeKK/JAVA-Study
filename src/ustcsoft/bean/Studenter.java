package ustcsoft.bean;

import java.text.DateFormat;
import java.util.Date;

public class Studenter {
	private String studenterID;// 学号
	private String studenterName;// 姓名
	private int age;// 年龄
	private Date startTime;// 入学时间
	private String text;// 备考

	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return "学号：" + this.getStudenterID() + "  " + "姓名：" + this.getStudenterName() + "  " + "年龄：" + this.getAge()
				+ "  " + "入学时间：" + this.getStartTime() + "  " + "备考：" + this.getText();
	}

	public String getStudenterID() {
		return studenterID;
	}

	public void setStudenterID(String studenterID) {
		this.studenterID = studenterID;
	}

	public String getStudenterName() {
		return studenterName;
	}

	public void setStudenterName(String studenterName) {
		this.studenterName = studenterName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getStartTime() {
		return DateFormat.getDateInstance(DateFormat.FULL).format(startTime);
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
