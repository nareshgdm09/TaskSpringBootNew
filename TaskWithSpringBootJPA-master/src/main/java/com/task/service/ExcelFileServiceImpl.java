package com.task.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.task.common.CommonUtil;
import com.task.common.Constants;
import com.task.entity.Player;
import com.task.exception.ExcelFileCreationException;

@Component
public class ExcelFileServiceImpl implements ExcelFileService {

	@Override
	public void createExcelFile(List<Player> players) throws ExcelFileCreationException {
		String excelpath = Constants.Excelpath;
		File file1 = new File(excelpath);
		if (!file1.exists()) {
			file1.mkdir();
		}
		for (Player player : players) {

			System.out.println("Processing Excel file player :" + player.getId() + " " + player.getName()
					+ player.getCreatedTime());
			// Create blank workbook
			XSSFWorkbook workbook = new XSSFWorkbook();

			// Create a blank sheet
			XSSFSheet spreadsheet = workbook.createSheet(" Player Info ");

			// Create row object
			XSSFRow row;
			try {
				String jsonString = CommonUtil.byteArrToString(player.getJsonFile());
				String xmlString = CommonUtil.byteArrToString(player.getXmlFile());

				JSONObject playerJson = StringToJsonObj(jsonString);

				Map<String, Object[]> empinfo = new TreeMap<String, Object[]>();

				empinfo.put("1", new Object[] { "ID", "NAME", "ADDRESS", "DOB", "RUNS", "WICKETS" });

				empinfo.put("2",
						new Object[] { playerJson.getInt("id"), playerJson.getString("name"),
								playerJson.getString("address"), playerJson.getString("dob"), playerJson.getInt("runs"),
								playerJson.getString("wickets") });

				// Iterate over data and write to sheet
				Set<String> keyid = empinfo.keySet();
				int rowid = 0;

				for (String key : keyid) {
					row = spreadsheet.createRow(rowid++);
					Object[] objectArr = empinfo.get(key);
					int cellid = 0;

					for (Object obj : objectArr) {
						Cell cell = row.createCell(cellid++);
						cell.setCellValue((String) obj);
					}
				}

				// Write the workbook in file system
				FileOutputStream out;

				out = new FileOutputStream(new File(excelpath + player.getId() + ".xlsx"));
				workbook.write(out);
				out.close();
				File file = new File(excelpath + player.getId() + ".xml");

				FileUtils.writeStringToFile(file, xmlString, "UTF-8");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				 throw new ExcelFileCreationException("Exception while creatinf Excel File");
			} finally {

			}

		}

	}

	static JSONObject StringToJsonObj(String jsonString) {
		try {
			return new JSONObject(jsonString);
		} catch (JSONException err) {
			System.out.println("error converting to josnObject");
		}
		return null;
	}

}
