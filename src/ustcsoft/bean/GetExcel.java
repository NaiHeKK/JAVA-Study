package ustcsoft.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetExcel {
	public static void main(String[] args) throws OpenXML4JException, IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		String excelPath = "homework_反射.xlsx";
		File excel = new File(excelPath);
		Map<String, List<?>> dataMap = getExcelData(excel);
		for (String key : dataMap.keySet()) {
			System.out.println(key + " Object List");
			List<?> stuList = dataMap.get(key);
			for (int i = 0; i < stuList.size(); i++) {
				System.out.println(stuList.get(i));
			}
		}
	}

	/*
	 * 根据sheet的名字获取该sheet里的数据
	 */
	public static void getSheetByName(Workbook wb, String name)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		Sheet sheet = wb.getSheet(name);
		getSheetData(sheet);
	}

	/*
	 * 打开excel并根据sheet的个数调用getSheetData
	 */
	public static Map<String, List<?>> getExcelData(File excel) throws OpenXML4JException, IOException,
			ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		if (excel.isFile() && excel.exists()) {
			String[] split = excel.getName().split("\\.");
			Workbook wb;
			if ("xls".equals(split[1])) {
				FileInputStream fis = new FileInputStream(excel);
				wb = new HSSFWorkbook(fis);
			} else if ("xlsx".equals(split[1])) {
				wb = new XSSFWorkbook(excel);
			} else {
				System.out.println("文件类型错误!");
				return null;
			}
			int sheetNum = wb.getNumberOfSheets();
			Map<String, List<?>> dataMap = new HashMap<String, List<?>>();
			for (int i = 1; i < sheetNum; i++) {
				Sheet sheet = wb.getSheetAt(i);
//				System.out.println("Sheet：" + sheet.getSheetName());
				List<?> objList = getSheetData(sheet);
				dataMap.put(sheet.getSheetName(), objList);
			}
			wb.close();
			return dataMap;
		} else {
			System.out.println("文件不存在");
			return null;
		}

	}

	/*
	 * 获取excel中每一个sheet里的数据
	 */
	public static List<?> getSheetData(Sheet sheet)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		int rowNum = sheet.getPhysicalNumberOfRows();// 获取此sheet的有效行数
		Row classRow = sheet.getRow(0);// 获取储存类完整路径的行
		String classString = classRow.getCell(1).toString();// 获取类的完整路径
		Class<?> myClass = Class.forName(classString);// 通过反射获取类
		Row filedRow = sheet.getRow(1);// 获取储存变量的行
		Row filedNameRow = sheet.getRow(2);// 获取储存变量名的行
		Row typeRow = sheet.getRow(3);// 获取储存变量类型的行
		List<String> filedList = new ArrayList<>();
		for (int j = 1; j < filedRow.getPhysicalNumberOfCells(); j++) {
			String filedString = filedRow.getCell(j).toString();
			filedList.add(filedString.substring(0, 1).toUpperCase() + filedString.substring(1));
		}
		List<String> filedNameList = new ArrayList<>();
		for (int j = 1; j < filedNameRow.getPhysicalNumberOfCells(); j++) {
			filedNameList.add(filedNameRow.getCell(j).toString());
		}

		int colNum = filedRow.getPhysicalNumberOfCells();// 获取此sheet的有效列数
		List<Object> objList = new ArrayList<>();
		for (int j = 4; j < rowNum; j++) {// 循环每一有效行
			Row row = sheet.getRow(j);
			Object obj = myClass.getDeclaredConstructor().newInstance();
			if (row != null) {
				for (int k = 1; k < colNum; k++) {// 循环每一有效列
					String typeString = typeRow.getCell(k).toString();
					if (typeString.equals("int")) {
						typeString = "java.lang.Integer";
					} else if (typeString.equals("Date")) {
						typeString = "java.util." + typeString;
					} else if (typeString.equals("BigDecimal")) {
						typeString = "java.math." + typeString;
					} else {
						typeString = "java.lang." + typeString;
					} // 组建变量的类的完整路径，供下面反射使用
					Method myMethod = myClass.getDeclaredMethod("set" + filedList.toArray()[k - 1],
							Class.forName(typeString));// 反射各变量的set方法
					if (typeRow.getCell(k).toString().equals("int")) {
						myMethod.invoke(obj, Double.valueOf(row.getCell(k).toString()).intValue());
					} else if (typeRow.getCell(k).toString().equals("Date")) {
						myMethod.invoke(obj, row.getCell(k).getDateCellValue());
					} else if (typeRow.getCell(k).toString().equals("String")) {
						myMethod.invoke(obj, row.getCell(k).toString());
					} else if (typeRow.getCell(k).toString().equals("BigDecimal")) {
						myMethod.invoke(obj, new BigDecimal(row.getCell(k).toString()));
					} // 通过反射调用各变量的set方法给变量赋值
				}
				objList.add(obj);
				// 将个变量赋值过的对象储存到objList中
			}
		}
//		printObjData(objList, myClass, filedList, filedNameList);
		return objList;
	}

	/*
	 * 通过反射获取对象列表objList里的各对象的各变量get方法并调用获取值，并打印在屏幕上
	 */
	public static void printObjData(List<?> objList, Class<?> myClass, List<String> filedList,
			List<String> filedNameList) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		System.out.println("==========" + myClass.getSimpleName() + " Start==========");
		for (Object obj : objList) {// 循环取出objList中的每一个对象
			int i = 0;
			for (String filed : filedList) {
				Method myMethod = myClass.getMethod("get" + filed);
				System.out.print(filedNameList.toArray()[i++] + "：" + myMethod.invoke(obj) + "  ");
				// 通过反射调用各变量的get方法获取变量的值，并打印
			}
			System.out.println();

		}
		System.out.println("============END============");
		System.out.println();
	}

}
