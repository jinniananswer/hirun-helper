package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.SecondInstallmentCollectionDTO;

public interface IOrderSecondInstallmentService {

    public void secondInstallmentCollect(SecondInstallmentCollectionDTO dto);
}
