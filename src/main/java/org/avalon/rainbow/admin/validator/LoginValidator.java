package org.avalon.rainbow.admin.validator;

import org.avalon.rainbow.admin.dto.LoginAccountDTO;
import org.avalon.rainbow.admin.entity.User;
import org.avalon.rainbow.admin.event.LoginEvent;
import org.avalon.rainbow.admin.service.CommonService;
import org.avalon.rainbow.common.constant.AppConst;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.EncryptionUtils;
import org.avalon.rainbow.common.utils.StringUtils;
import org.avalon.rainbow.common.validation.AbstractValidator;
import org.springframework.context.ApplicationContext;

import java.util.Date;

public class LoginValidator extends AbstractValidator {

    private final ApplicationContext applicationContext = BeanUtils.getApplicationContext();

    @Override
    protected void validate(Object object) {
        boolean isEntered = true;
        Date now = new Date();

        LoginAccountDTO dto = (LoginAccountDTO) object;
        if (StringUtils.isBlank(dto.getUsername())) {
            addViolation("username", "Please enter username");
            isEntered = false;
        }
        if (StringUtils.isBlank(dto.getPassword())) {
            addViolation("username", "Please enter password");
            isEntered = false;
        }

        if (isEntered) {
            User user = dto.getUser();
            if (user == null) {
                addViolation("password", "User doesn't exist or password is incorrect");
            } else {
                String status = user.updateStatus();
                boolean isPwdMatch = EncryptionUtils.matchPassword(dto.getPassword(), user.getPassword());
                if (!isPwdMatch) {
                    addViolation("password", "User doesn't exist or password is incorrect");
                    applicationContext.publishEvent(new LoginEvent(this, user, AppConst.NO));
                } else {
                    if (AppConst.ACCOUNT_STATUS_EXPIRED.equals(status)) {
                        addViolation("password", "Account expired");
                    } else if (AppConst.ACCOUNT_STATUS_LOCKED.equals(status)) {
                        addViolation("password", "Account Locked");
                    } else if (AppConst.ACCOUNT_STATUS_CANCELLED.equals(status)) {
                        addViolation("password", "Account Cancelled");
                    } else if (AppConst.ACCOUNT_STATUS_INACTIVE.equals(status)) {
                        addViolation("password", "Account Inactive");
                    }
                    applicationContext.publishEvent(new LoginEvent(this, user, user.getStatus()));
                }
            }
        }
    }
}
