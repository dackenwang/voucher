package com.boc.voucherjavademo.web;

import com.boc.voucherjavademo.domain.Voucher;
import com.boc.voucherjavademo.domain.VoucherRepository;
import com.boc.voucherjavademo.dto.VoucherFindDto;
import com.boc.voucherjavademo.dto.VoucherQueryDto;
import com.boc.voucherjavademo.dto.VoucherSaveDto;
import com.boc.voucherjavademo.dto.VoucherUpdateDto;
import com.boc.voucherjavademo.service.ExcelService;
import com.boc.voucherjavademo.service.VoucherService;
import com.boc.voucherjavademo.tools.AbstractController;
import com.train.base.auth.Authorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Slf4j
@RestController
@Tag(name = "传票服务定义")
@RequestMapping("/bocsh-voucher-demo/services")
public class VoucherController extends AbstractController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private ExcelService excelService;

    @Operation(summary = "新增传票对象",description = "新增传票对象")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Authorize({"MSA_ADMIN"})
    public void addVoucher(@Validated @RequestBody VoucherSaveDto voucherSaveDto,HttpServletResponse response
    ) throws Exception{
        voucherService.add(voucherSaveDto);
    }

    @Operation(summary = "查询传票对象",description = "查询传票对象")
    @GetMapping("/{id}")
    @Authorize({"MSA_USER","MSA_ADMIN"})
    public VoucherFindDto getVoucherById(@PathVariable Long id, HttpServletResponse response) throws Exception {
        if (voucherService.exists(id)) {
            return voucherService.find(id);
        }else  {
            response.sendError(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }

    @Operation(summary = "删除传票对象",description = "删除传票对象")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Authorize({"MSA_ADMIN"})
    public void deleteVoucherById(@PathVariable Long id, HttpServletResponse response) throws Exception {
        if (voucherService.exists(id)) {
            voucherService.delete(id);
        }
        else response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @Operation(summary = "更新传票对象",description = "更新传票对象")
    @PutMapping("/{id}")
    @Authorize({"MSA_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    public void updateVoucherById(@PathVariable Long id,
                                  @RequestBody @Validated VoucherUpdateDto voucherUpdateDto,
                                  HttpServletResponse response) throws Exception {
        if (voucherService.exists(id)) {
            voucherService.update(voucherUpdateDto,id);
        }
        else response.sendError(HttpStatus.NOT_FOUND.value());

    }

    @Operation(summary = "查询传票列表",description = "查询传票列表")
    @PostMapping("/search")
    @Authorize({"MSA_USER","MSA_ADMIN"})
    @PageableAsQueryParam //让分页参数生成swagger文档时候显示为查询参数
    public Page<VoucherFindDto> getVouchers(@Parameter Pageable pageable, @RequestBody @Validated VoucherQueryDto voucherQueryDto) throws Exception {
        return voucherService.queryList(voucherQueryDto,pageable);
    }

    @Operation(summary = "批量导出传票对象",description = "批量导出传票对象")
    @PostMapping("/download")
    @Authorize({"MSA_USER","MSA_ADMIN"})
    public void download(@Validated @RequestBody VoucherQueryDto voucherQueryDto,HttpServletResponse response) throws Exception {
        //保存到工作簿
        XSSFWorkbook workbook = excelService.createVoucherExcel(voucherQueryDto);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=voucherExcel.xlsx");
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @Operation(summary = "批量导入传票对象",description = "批量导入传票对象")
    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Authorize({"MSA_ADMIN"})
    public void upload(@RequestParam("img") MultipartFile file, HttpServletResponse response) throws Exception {
        String filename = file.getOriginalFilename();
        log.info("filename:{}",filename);
        if (filename.endsWith(".xlsx")) {
            InputStream inputStream = file.getInputStream();
            if (inputStream != null) {
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                excelService.parseVoucherExcel(workbook);
            }
        }
        else  response.sendError(HttpStatus.BAD_REQUEST.value(),"文件必须为xlsx格式");
    }

//    @GetMapping("/check")
//    @Operation(summary = "检查传票凭证号是否存在",description = "检查传票凭证号是否存在")
//    @Authorize({"MSA_USER","MSA_ADMIN"})
//    public boolean existsVoucher(@RequestParam("tranNo") String tranNo) {
//        return voucherService.existsByTranNo(tranNo);
//    }


}
