package com.haritbrij.haritBrij;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.haritbrij.haritBrij.models.Organisation;

import java.util.ArrayList;
import java.util.List;

public class AdminViewModel extends AndroidViewModel {
    SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getApplication().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    private SharedPreferences.Editor editor;

    public AdminViewModel(@NonNull Application application) {
        super(application);
        editor = sharedPreferences.edit();
    }

    private final MutableLiveData<List<Organisation>> orgList = new MutableLiveData<List<Organisation>>();

    public void addOrg(Organisation org) {
        List<Organisation> tempTreeList = orgList.getValue();
        if (tempTreeList != null) {
            tempTreeList.add(org);
        }
        orgList.setValue(tempTreeList);
    }

    public void setOrgList(List<Organisation> orgList) {
        this.orgList.setValue(orgList);
    }

    public ArrayList<Organisation> getOrgListj() {
        return (ArrayList<Organisation>) orgList.getValue();
    }

}
