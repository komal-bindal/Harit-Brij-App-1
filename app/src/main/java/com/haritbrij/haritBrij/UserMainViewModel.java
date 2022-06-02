package com.haritbrij.haritBrij;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.haritbrij.haritBrij.models.Tree;

import java.util.List;

public class UserMainViewModel extends AndroidViewModel {
    public UserMainViewModel(@NonNull Application application) {
        super(application);
    }

    private final MutableLiveData<List<Tree>> treeList = new MutableLiveData<List<Tree>>();

    public void addTree(Tree tree) {
        List<Tree> tempTreeList = treeList.getValue();
        if (tempTreeList != null) {
            tempTreeList.add(tree);
        }
        treeList.setValue(tempTreeList);
    }

    public List<Tree> getTreeList() {
        return treeList.getValue();
    }
}
