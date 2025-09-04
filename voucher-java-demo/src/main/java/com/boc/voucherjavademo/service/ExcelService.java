package com.boc.voucherjavademo.service;

import com.boc.voucherjavademo.domain.TranType;
import com.boc.voucherjavademo.domain.Voucher;
import com.boc.voucherjavademo.domain.VoucherRepository;
import com.boc.voucherjavademo.dto.VoucherFindDto;
import com.boc.voucherjavademo.dto.VoucherParserDto;
import com.boc.voucherjavademo.dto.VoucherQueryDto;
import com.boc.voucherjavademo.dto.VoucherSaveDto;
import com.boc.voucherjavademo.tools.SpringValidatorUtil;
import com.train.base.utils.DateUtils;
import com.train.base.utils.DtoUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ExcelService {
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private SpringValidatorUtil springValidatorUtil;

    @Autowired
    Validator validator;

    /**
     * 将查询结果保存至excel
     * @param voucherQueryDto
     * @return
     */
    public XSSFWorkbook createVoucherExcel(VoucherQueryDto voucherQueryDto) {
        List<VoucherFindDto> voucherList = voucherService.queryListAll(voucherQueryDto);
        //声明一个工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //生成一个表格
        XSSFSheet sheet = workbook.createSheet("Sheet1");

        sheet.setDefaultColumnWidth((short)18);
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("传票凭证号");
        header.createCell(1).setCellValue("科目");
        header.createCell(2).setCellValue("付款账号");
        header.createCell(3).setCellValue("付款账户名");
        header.createCell(4).setCellValue("收款账号");
        header.createCell(5).setCellValue("收款账户名");
        header.createCell(6).setCellValue("交易金额");
        header.createCell(7).setCellValue("摘要");
        header.createCell(8).setCellValue("交易日期");
        header.createCell(9).setCellValue("交易柜员号");

        int rowNum = 1;
        for (VoucherFindDto voucherFindDto : voucherList) {
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(voucherFindDto.getTranNo());
            row.createCell(1).setCellValue(voucherFindDto.getTranType().getCode());
            row.createCell(2).setCellValue(voucherFindDto.getPayerAccNo());
            row.createCell(3).setCellValue(voucherFindDto.getPayerAccName());
            row.createCell(4).setCellValue(voucherFindDto.getPayeeAccNo());
            row.createCell(5).setCellValue(voucherFindDto.getPayeeAccName());
            row.createCell(6).setCellValue(voucherFindDto.getAmt());
            row.createCell(7).setCellValue(voucherFindDto.getRemarks());
            row.createCell(8).setCellValue(voucherFindDto.getTranDate());
            row.createCell(9).setCellValue(voucherFindDto.getTellerNo());
        }
        return workbook;

    }

    public void parseVoucherExcel(XSSFWorkbook workbook) {
        XSSFSheet xssfSheet = workbook.getSheetAt(0);
        int rowNum = xssfSheet.getLastRowNum();
        List<Voucher> voucherList = new ArrayList<>();
        for (int i = 1; i <= rowNum; i++) {
            Row row = xssfSheet.getRow(i);
            VoucherParserDto voucherParserDto = new VoucherParserDto();

            voucherParserDto.setTranNo(row.getCell(0).getStringCellValue());
            voucherParserDto.setTranType(TranType.findByCode(row.getCell(1).getStringCellValue()));
            voucherParserDto.setPayerAccNo(row.getCell(2).getStringCellValue());
            voucherParserDto.setPayerAccName(row.getCell(3).getStringCellValue());
            voucherParserDto.setPayeeAccNo(row.getCell(4).getStringCellValue());
            voucherParserDto.setPayeeAccName(row.getCell(5).getStringCellValue());
            voucherParserDto.setAmt(row.getCell(6).getNumericCellValue());
            voucherParserDto.setRemarks(row.getCell(7).getStringCellValue());
            voucherParserDto.setTranDate(row.getCell(8).getStringCellValue());
            voucherParserDto.setTellerNo(row.getCell(9).getStringCellValue());

            springValidatorUtil.validate(voucherParserDto);

//            String message = "";
//            //对每行数据进行校验
//            Set<ConstraintViolation<VoucherParserDto>> set =
//                    Validation.buildDefaultValidatorFactory().getValidator().validate(voucherParserDto);
//            if (!set.isEmpty()) {
//                for (ConstraintViolation<VoucherParserDto> constraintViolation : set) {
//                    message = message + constraintViolation.getPropertyPath().toString() +
//                            "(" + constraintViolation.getInvalidValue() + ")" +
//                            constraintViolation.getMessage() + ",";
//                }
//            }
//            if (!message.equals("")) {
//                throw new IllegalArgumentException("第" + (i+1)+ "行数据校验错误："+ message.substring(0, message.length()-1));
//            }
            Voucher voucher = DtoUtils.convertNew(voucherParserDto, Voucher.class);
            voucher.setTranTime(DateUtils.getCurrentDateString("yyyy-MM-dd HH:mm:ss"));

            voucherList.add(voucher);

        }
        voucherRepository.saveAll(voucherList);
    }





}
