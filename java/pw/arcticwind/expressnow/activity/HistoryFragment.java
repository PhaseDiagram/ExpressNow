package pw.arcticwind.expressnow.activity;

import java.io.File;

//历史页
public class HistoryFragment extends FavorCellFragment {

    @Override
    void setTitle() {
        getActivity().setTitle("历史");
    }

    @Override
    File getDir() {
        return new File(getContext().getFilesDir().toString() + File.separator + "history");
    }

}
