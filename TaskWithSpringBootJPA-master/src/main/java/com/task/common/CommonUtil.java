package com.task.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.entity.Player;
import com.task.exception.ErrorFileException;

public class CommonUtil {
	public static void findCommonFiles(List<String> list1, List<String> list2) {
		list1.retainAll(list2);
		list2.retainAll(list1);
	}

	public static String removeFileExt(File file) {
		return FilenameUtils.removeExtension(file.getName());
	}

	public static byte[] readFileToByteArray(File filePath) {
		String path = filePath.getPath();
		byte[] byteArr = null;
		try {
			byteArr = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {

		}
		return byteArr;
	}

	public static String byteArrToString(byte[] byteArr) {
		return new String(byteArr);
	}

	public static Player FileToObject(String fileName) throws ErrorFileException {
		Player player = new Player();
		ObjectMapper mapper = new ObjectMapper();
		try {
			byte[] jsonData = Files.readAllBytes(Paths.get(Constants.JSONPATH + fileName + Constants.JSONEXT));

			byte[] xmlData = Files.readAllBytes(Paths.get(Constants.XMLPATH + fileName + Constants.XMLEXT));
			player = mapper.readValue(jsonData, Player.class);
			player.setId(Integer.valueOf(fileName));
			player.setCreatedTime(new Date());
			player.setStatus("Playing");
			player.setJsonFile(jsonData);
			player.setXmlFile(xmlData);

		} catch (Exception e) {
			throw new ErrorFileException("Error File Record :");
		}

		return player;
	}

}
