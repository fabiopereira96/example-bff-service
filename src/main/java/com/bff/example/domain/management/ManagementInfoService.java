package com.bff.example.domain.management;

import com.bff.example.configuration.JHipsterInfo;
import com.bff.example.domain.management.model.ManagementInfo;
import io.quarkus.runtime.configuration.ProfileManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
* Provides information for management/info resource
*/
@ApplicationScoped
public class ManagementInfoService {

    private final JHipsterInfo JHipsterInfo;

    @Inject
    public ManagementInfoService(JHipsterInfo JHipsterInfo) {
        this.JHipsterInfo = JHipsterInfo;
    }

    public ManagementInfo getManagementInfo(){
        var info = new ManagementInfo();
        if(JHipsterInfo.isEnable()){
            info.activeProfiles.add("swagger");
        }
        info.activeProfiles.add(ProfileManager.getActiveProfile());
        info.displayRibbonOnProfiles = ProfileManager.getActiveProfile();
        return info;
    }
}

