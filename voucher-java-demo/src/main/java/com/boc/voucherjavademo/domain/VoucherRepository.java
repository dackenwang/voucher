package com.boc.voucherjavademo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 继承JpaSpecificationExecutor<Voucher>实现Specification 动态查询
 */
public interface VoucherRepository extends JpaRepository<Voucher, Long>, JpaSpecificationExecutor<Voucher> {
    boolean existsByTranNo(String tranNo);
}
