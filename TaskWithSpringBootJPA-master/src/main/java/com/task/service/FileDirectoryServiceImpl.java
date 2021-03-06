package com.task.service;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.task.common.CommonUtil;
import com.task.common.Constants;
import com.task.entity.Player;
import com.task.exception.FileMoveException;

@Component
public class FileDirectoryServiceImpl implements FileDirectoryService {

	private static final Logger logger = LoggerFactory.getLogger(FileDirectoryServiceImpl.class);

	@Override
	public List<String> getTypeFiles(String dirPath, String fileType) {
		logger.info("inside getTypeFiles(String dirPath, String fileType) method");
		List<String> listFiles = new ArrayList<>();
		File directory = new File(dirPath);
		File[] jsonFiles = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(fileType);
			}
		});
		Arrays.stream(jsonFiles).forEach(file -> listFiles.add(CommonUtil.removeFileExt(file)));
		return listFiles;
	}

	@Override
	public void moveCompletedFiles(List<Player> playerRecords) throws FileMoveException {
		logger.info("inside moveCompletedFiles(List<Player> playerRecords) method");
		for (Player player : playerRecords) {
			System.out.println("Moving file :" + player);
			try {

				Path jsonSourcePath = Paths.get(Constants.JSONPATH + player.getId() + Constants.JSONEXT);
				Path jsonDestinationPath = Paths.get(Constants.JSONCOMPLTEDPATH + player.getId() + Constants.JSONEXT);

				Path xmlSourcePath = Paths.get(Constants.XMLPATH + player.getId() + Constants.XMLEXT);
				Path xmlDestinationPath = Paths.get(Constants.XMLCOMPLETEDPATH + player.getId() + Constants.XMLEXT);

				Files.move(jsonSourcePath, jsonDestinationPath, StandardCopyOption.REPLACE_EXISTING);
				Files.move(xmlSourcePath, xmlDestinationPath, StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e) {
				logger.info("Error Record :"+player);
				throw new FileMoveException("File Moving Exception");
			}
		}
	}

	@Override
	public void moveErrorFiles(List<String> errorRecords) throws FileMoveException {

		for (String errorRecord : errorRecords) {

			try {
				Path jsonSourcePath = Paths.get(Constants.JSONPATH + errorRecord + Constants.JSONEXT);
				Path jsonDestinationPath = Paths.get(Constants.JSONERRORDIR + errorRecord + Constants.JSONEXT);

				Path xmlSourcePath = Paths.get(Constants.XMLPATH + errorRecord + Constants.XMLEXT);
				Path xmlDestinationPath = Paths.get(Constants.XMLERRORDIR + errorRecord + Constants.XMLEXT);

				Files.move(jsonSourcePath, jsonDestinationPath, StandardCopyOption.REPLACE_EXISTING);
				Files.move(xmlSourcePath, xmlDestinationPath, StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new FileMoveException("File Moving Exception");
			}
		}
	}
}
