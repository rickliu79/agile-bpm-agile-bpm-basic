package com.dstz.sys.scheduler;

import com.dstz.sys.BasicTest;
import com.dstz.sys.core.dao.SysScheduleJobDao;
import com.dstz.sys.core.model.SysScheduleJob;
import org.junit.Test;

/**
 * @author didi
 */
public class QuartzManagerServiceTest extends BasicTest {

    private QuartzManagerService getQuartzManager() {

        return getApplicationContext().getBean(QuartzManagerService.class);
    }

}
