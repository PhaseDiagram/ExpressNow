package pw.arcticwind.expressnow.activity;

import java.io.File;

//收藏页
public class FavoriteFragment extends FavorCellFragment {

    @Override
    void setTitle() {
        getActivity().setTitle("收藏");
    }

    @Override
    File getDir() {
        return new File(getContext().getFilesDir().toString() + File.separator + "favor");
    }

}
