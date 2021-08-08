package com.example.administrator.benotes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

/**
 * Created by Raken on 4/5/2018.
 */

public class pdf extends Fragment{
    @Nullable


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Pdf Viewer");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.pdf_layout ,container,false);
        String  Notename=null;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Notename = bundle.getString("Notename", Notename);
        }
        PDFView pdfView=(PDFView)v.findViewById(R.id.pdfView);
        pdfView.fromAsset(Notename).load();
                return v;
    }
}
