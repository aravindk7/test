package com.sample.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.domain.FundPerformance;
import com.sample.service.CSVProcessor;
import com.sample.service.FundsReportService;
import com.sample.service.RequestProcessor;

/**
 * @author Aravind
 *
 */
@Service
public class RequestProcessorImpl implements RequestProcessor {

	@Autowired
	private CSVProcessor csvProcessor;

	@Autowired
	private FundsReportService fundsReportService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sample.service.RequestProcessor#processInputFiles(java.lang.String)
	 */
	@Override
	public void processInputFiles(String path) {
		File[] files = listFilesInFolder(path);

		if (files == null) {
			return;
		}

		for (File file : files) {
			try {
				csvProcessor.pareCSV(file);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * Return the list of CSV Files in the given path
	 * 
	 * @param path
	 * @return
	 */
	public File[] listFilesInFolder(String path) {
		File folder = new File(path);
		if (folder.isDirectory()) {
			return folder.listFiles((dir, name) -> name.endsWith(".csv"));
		}
		if (folder.isFile() && folder.getName().endsWith(".csv")) {
			return new File[] { folder };
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sample.service.RequestProcessor#generateMonthlyReport()
	 */
	@Override
	public void generateMonthlyReport() {
		List<FundPerformance> funds = fundsReportService.computeReturns();
		csvProcessor.writeCSV(funds);
	}

}
