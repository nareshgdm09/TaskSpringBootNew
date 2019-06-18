package com.task.service;

import java.util.List;

import com.task.entity.Player;
import com.task.exception.ExcelFileCreationException;


public interface ExcelFileService {
	public void createExcelFile(List<Player> players) throws ExcelFileCreationException;
}
