package edu.android.itwill.project_oil_hub.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import static edu.android.itwill.project_oil_hub.Model.OilConstant.*;

public class SingleDialog extends DialogFragment {
    public interface SingleChoiceDlgCallback{
        void onClickOilTypeSelect(int position);
    }

    private SingleChoiceDlgCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SingleChoiceDlgCallback){
            callback = (SingleChoiceDlgCallback) context;
        }
    }// end onAttach

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }

    public Dialog onCreateDialog(Bundle saveInstanceState){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setTitle("관심 유종 선택");
        String[] items = PRODUCTS;
        builder.setItems(items, (dialog, which) -> {
            callback.onClickOilTypeSelect(which);
        });

        return builder.create();
    }
}
