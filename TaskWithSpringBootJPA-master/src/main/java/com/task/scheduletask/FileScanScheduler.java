package com.task.scheduletask;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.task.common.CommonUtil;
import com.task.common.Constants;
import com.task.entity.Player;
import com.task.exception.FileMoveException;
import com.task.service.FileDirectoryService;
import com.task.service.PlayerDBService;

@Component
public class FileScanScheduler {
	private static final Logger logger = LoggerFactory.getLogger(FileScanScheduler.class);
	@Autowired
	FileDirectoryService fileDirectoryService;

	@Autowired
	PlayerDBService PlayerDBService;

	@SuppressWarnings("unchecked")
	@Scheduled(initialDelay = 1000, fixedRate = 8000)
	public void DirectoryScanScheduledMethod() {
		logger.info("inside modelTrainerscheduledMethod()");
		
		List<String> jsonFiles = fileDirectoryService.getTypeFiles(Constants.JSONPATH, Constants.JSONEXT);
		List<String> xmlFiles = fileDirectoryService.getTypeFiles(Constants.XMLPATH, Constants.XMLEXT);

		if (jsonFiles.size() > 0 && xmlFiles.size() > 0) {
			CommonUtil.findCommonFiles(jsonFiles, xmlFiles);

			List<Object> validAndErrorRecords = PlayerDBService.savePlayers(jsonFiles, xmlFiles);

			List<Player> validRecords = (List<Player>) validAndErrorRecords.get(0);
			List<String> ErrorRecords = (List<String>) validAndErrorRecords.get(1);

			try {
				if (validRecords.size() > 0)
					fileDirectoryService.moveCompletedFiles(validRecords);
				if (ErrorRecords.size() > 0)
					fileDirectoryService.moveErrorFiles(ErrorRecords);
			} catch (FileMoveException e) {
				logger.info("Error while moving File");
				e.printStackTrace();
			}
		}
	}
}
