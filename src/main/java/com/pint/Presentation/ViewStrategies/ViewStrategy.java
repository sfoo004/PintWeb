package com.pint.Presentation.ViewStrategies;

import com.pint.Presentation.ViewModels.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dionny on 11/26/2015.
 */
public abstract class ViewStrategy<T> {
    public ViewModel CreateViewModel(T model) {
        return performMapping(model);
    }

    public List<ViewModel> CreateViewModel(Iterable<T> models) {
        List<ViewModel> out = new ArrayList<ViewModel>();
        for (T model :
                models) {
            out.add(performMapping(model));
        }
        return out;
    }

    private ViewModel performMapping(T model) {
        if(model == null){
            return defaultMapping();
        }
        return mapObject(model);
    }

    protected abstract ViewModel mapObject(T model);
    protected abstract ViewModel defaultMapping();
}
