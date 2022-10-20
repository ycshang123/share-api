package com.ycshang.userservice.rocketmq;


import com.ycshang.userservice.domain.dto.UserAddBonusDto;
import com.ycshang.userservice.domain.entity.BonusEventLog;
import com.ycshang.userservice.domain.entity.User;
import com.ycshang.userservice.repository.BonusEventLogRepository;
import com.ycshang.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RocketMQMessageListener(consumerGroup = "consumer", topic = "add-bonus")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusListener implements RocketMQListener<UserAddBonusDto> {
    private final UserRepository userRepository;
    private final BonusEventLogRepository bonusEventLogRepository;

    @Override
    public void onMessage(UserAddBonusDto userAddBonusDto) {
//       为用户增加积分
        Integer userId = userAddBonusDto.getUserId();
        ;
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setBonus(user.getBonus() + userAddBonusDto.getBonus());
            userRepository.saveAndFlush(user);
        }

//        写积分日志
        bonusEventLogRepository.save(BonusEventLog.builder()
                .userId(userId)
                .value(userAddBonusDto.getBonus()).event("CONTRIBUTE").createTime(new Date()).description("投稿增加积分").build());

    }
}
