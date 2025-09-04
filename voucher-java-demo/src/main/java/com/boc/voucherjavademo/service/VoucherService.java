package com.boc.voucherjavademo.service;

import com.boc.voucherjavademo.domain.Voucher;
import com.boc.voucherjavademo.domain.VoucherRepository;
import com.boc.voucherjavademo.dto.VoucherFindDto;
import com.boc.voucherjavademo.dto.VoucherQueryDto;
import com.boc.voucherjavademo.dto.VoucherSaveDto;
import com.boc.voucherjavademo.dto.VoucherUpdateDto;
import com.boc.voucherjavademo.tools.TellerUtils;
import com.train.base.utils.DateUtils;
import com.train.base.utils.DtoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    Validator validator;

    /**
     * 新增传票信息
     * @param voucherSaveDto
     */
    public void add(VoucherSaveDto voucherSaveDto) {
        Voucher voucher = DtoUtils.convertNew(voucherSaveDto, Voucher.class);
        voucher.setTellerNo(TellerUtils.generateTellerNo());
        voucher.setTranTime(DateUtils.getCurrentDateString("yyyy-MM-dd HH:mm:ss"));
        voucherRepository.save(voucher);
    }

    /**
     * 根据id查询传票对象
     * @param id
     * @return voucherfinddto
     */
    public VoucherFindDto find(Long id) {
        Voucher voucher = voucherRepository.findById(id).get();
        return DtoUtils.convertNew(voucher, VoucherFindDto.class);
    }


    /**
     * 根据id查询传票对象是否存在
     * @param id
     * @return
     */
    public boolean exists(Long id) {
        return voucherRepository.existsById(id);
    }

    /**
     * 查询传票凭证号是否存在
     * @param tranNo
     * @return
     */
    public boolean existsByTranNo(String tranNo) {
        return voucherRepository.existsByTranNo(tranNo);
    }

    /**
     * 根据id删除传票对象
     * @param id
     */
    public void delete(Long id) {
        voucherRepository.deleteById(id);

    }

    /**
     * 根据id和更新的传票对象（voucherUpdateDto）保存至voucher对象
     * @param voucherUpdateDto
     * @param id
     */
    public void update(VoucherUpdateDto voucherUpdateDto, Long id) {
        Voucher voucher = voucherRepository.findById(id).get();
        DtoUtils.convertUpdate(voucherUpdateDto,voucher);//将voucherUpdateDto对象转为voucher对象
        voucher.setTranTime(DateUtils.getCurrentDateString("yyyy-MM-dd HH:mm:ss"));
        voucherRepository.save(voucher);
    }
    public void update2(VoucherSaveDto voucherSaveDto, Long id) {
        Voucher voucher = voucherRepository.findById(id).get();
        DtoUtils.convertUpdate(voucherSaveDto,voucher);//将voucherSaveDto对象转为voucher对象
        voucher.setTranTime(DateUtils.getCurrentDateString("yyyy-MM-dd HH:mm:ss"));
        voucherRepository.save(voucher);
    }

    /**
     * 根据查询对象voucherQueryDto，分页参数pageable返回分页Page对象
     * @param voucherQueryDto
     * @param pageable
     * @return
     */
    public Page<VoucherFindDto> queryList(VoucherQueryDto voucherQueryDto, Pageable pageable) {
        return DtoUtils.convertPageToDto(voucherRepository.findAll(getWhereClause(voucherQueryDto),pageable),VoucherFindDto.class,pageable);
    }

    /**
     * 根据查询对象voucherQueryDto，不带分页查询所有
     * @param voucherQueryDto
     * @return
     */
    public List<VoucherFindDto> queryListAll(VoucherQueryDto voucherQueryDto) {
        //查询结果是List<Voucher>，遍历列表进行转换
        List<Voucher> voucherList = voucherRepository.findAll(getWhereClause(voucherQueryDto));
        List<VoucherFindDto> voucherFindDtoList = new ArrayList<>();
        for (Voucher voucher : voucherList) {
            VoucherFindDto voucherFindDto = DtoUtils.convertNew(voucher, VoucherFindDto.class);
            voucherFindDtoList.add(voucherFindDto);
        }
        return voucherFindDtoList;

    }

    /**
     * 基于Specification实现复杂查询，范围查询+模糊查询
     * @param voucherQueryDto
     * @return
     */
    private Specification<Voucher> getWhereClause(final  VoucherQueryDto voucherQueryDto) {
        return new Specification<Voucher>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                ArrayList<Predicate> predicate = new ArrayList<>();
                //1. 交易日期范围查询,必填
                //1.1 起始交易日期不为空
                if (!StringUtils.isEmpty(voucherQueryDto.getStartTranDate())) {
                    System.out.println("startTranDate:"+voucherQueryDto.getStartTranDate());
                    predicate.add(cb.greaterThanOrEqualTo(root.get("tranDate"), voucherQueryDto.getStartTranDate()));
                }
                //1.2 结束交易日期不为空
                if (!StringUtils.isEmpty(voucherQueryDto.getEndTranDate())) {
                    System.out.println("endTranDate:"+voucherQueryDto.getEndTranDate());
                    predicate.add(cb.lessThanOrEqualTo(root.get("tranDate"), voucherQueryDto.getEndTranDate()));
                }
                //1.3 交易日期都为空
//                if (StringUtils.isEmpty(voucherQueryDto.getStartTranDate())&&StringUtils.isEmpty(voucherQueryDto.getEndTranDate())) {
//                    throw new IllegalArgumentException("交易日期为必填项");
//                }
                //2. 交易金额范围查询
                if (voucherQueryDto.getMinAmount()!=null){
                    predicate.add(cb.greaterThanOrEqualTo(root.get("amt"), voucherQueryDto.getMinAmount()));
                }
                if (voucherQueryDto.getMaxAmount()!=null){
                    predicate.add(cb.lessThanOrEqualTo(root.get("amt"), voucherQueryDto.getMaxAmount()));
                }
                //3. 交易类型匹配精确查询
                if (voucherQueryDto.getTranType()!=null) {
                    predicate.add(cb.equal(root.get("tranType"), voucherQueryDto.getTranType()));
                }
                //4. 付款账号、付款账户名、收款账号、收款账户名模糊查询
                if (!StringUtils.isEmpty(voucherQueryDto.getPayerAccNo())){
                    predicate.add(cb.like(root.get("payerAccNo"), "%"+voucherQueryDto.getPayerAccNo()+"%"));
                }
                if (!StringUtils.isEmpty(voucherQueryDto.getPayerAccName())){
                    predicate.add(cb.like(root.get("payerAccName"), "%"+voucherQueryDto.getPayerAccName()+"%"));
                }
                if (!StringUtils.isEmpty(voucherQueryDto.getPayeeAccNo())){
                    predicate.add(cb.like(root.get("payeeAccNo"), "%"+voucherQueryDto.getPayeeAccNo()+"%"));
                }
                if (!StringUtils.isEmpty(voucherQueryDto.getPayeeAccName())){
                    predicate.add(cb.like(root.get("payeeAccName"), "%"+voucherQueryDto.getPayeeAccName()+"%"));
                }

                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

}
