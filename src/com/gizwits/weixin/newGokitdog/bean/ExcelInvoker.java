package com.gizwits.weixin.newGokitdog.bean;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



public class ExcelInvoker {
	
	private static Logger logger=Logger.getLogger(ExcelInvoker.class);
	
	private String excelFullPath;	
	
	public String getExcelFullPath() {
		return excelFullPath;
	}

	public void setExcelFullPath(String excelFullPath) {
		this.excelFullPath = excelFullPath;
	}

	/**
	 * Excel sheet转List
	 * @param hssfSheet
	 * @return
	 */
	public List<String> ExcelSheetToList(HSSFSheet hssfSheet){		
		List<String> macs=new ArrayList<String>();	

		for(int rowNum=0;rowNum<=hssfSheet.getLastRowNum();rowNum++){
			HSSFRow hssfRow = hssfSheet.getRow(rowNum);				
			if (hssfRow == null) {
                continue;
            }				
			//行转实体
			String mac=this.RowToEntity(hssfRow);				
			
			if(macs!=null){
				macs.add(mac);
			}			
		}		
		return macs;
	}
	
	/**
	 * 行转实体
	 * @param hssfRow
	 * @return shopInfo
	 */
	public String RowToEntity(HSSFRow hssfRow){

		return CellContentToValue(hssfRow.getCell(0));

	}
	
	/**
	 * 得到Excel表中的值
	 * @param hssfCell
	 * @return
	 */
	public String CellContentToValue(HSSFCell hssfCell){		
		
		hssfCell.setCellType(hssfCell.CELL_TYPE_STRING);
		return String.valueOf(hssfCell.getStringCellValue());
	}
	
	/**
	 * 读取Excel
	 * @return
	 * @throws IOException
	 */
	public List<String> ReadExcel() throws IOException{		
		List<String> results=new ArrayList<String>();
		InputStream stream=null;
		try {
			stream=new FileInputStream(this.excelFullPath);	
			logger.info("@INFO-输出-上报的文件地址为："+this.excelFullPath);
			logger.info("@INFO-输出-输出Excel文件流："+stream);
			
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(stream);		
			
			int sheetIndex=0;				
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(sheetIndex);
			results=this.ExcelSheetToList(hssfSheet);	
		} catch (Exception e) {
			logger.info("@INFO-输出-读取Excel出现异常，异常信息："+e.getMessage());
			e.printStackTrace();
		}finally{
			stream.close();
		}				
		return results;
	}
	
	public JSONObject ValidateExcelTemplate() throws IOException{
		JSONObject jsonObject=new JSONObject();
		
		boolean flag=true;
		String msg="";
		
		InputStream excelStream = null;
		
		try {
			excelStream=new FileInputStream(this.excelFullPath);	
			logger.info("@INFO-输出-上报的文件地址为："+this.excelFullPath);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(excelStream);			
			int sheetIndex=0;			
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(sheetIndex);
			
			for(int rowNum=0;rowNum<=hssfSheet.getLastRowNum();rowNum++){
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);				
				if (hssfRow == null) {
	                continue;
	            }			
				
				//验证行
				//第一列
				HSSFCell firstCell = hssfRow.getCell(0);
				
				
				if(firstCell==null){
					flag=false;
					msg="您上传的模板不符合规则，请审核之后，重新上传！";
					break;
				}
			}		
		}catch(Exception e){
			flag=false;
			msg="您上传的模板不符合规则，请审核之后，重新上传！";
			logger.info("@INFO-输出-验证Excel不通过，出现异常，异常信息："+e.getMessage());
			e.printStackTrace();
		}finally{
			excelStream.close();
		}
		
		
		jsonObject.put("flag", flag);
		jsonObject.put("msg", msg);
		
		return jsonObject;
	}
}
