package pw.arcticwind.expressnow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.model.AutoCompleteTextViewWithDel;
import pw.arcticwind.expressnow.utils.NetUtils;

//查询页
//Drawer 的一部分
public class QueryFragment extends Fragment implements View.OnClickListener{
    private Spinner spinner;
    private Button querySubmit;
    private View view;
    private ArrayAdapter<String> spinnerAdapter;
    private AutoCompleteTextViewWithDel autoCompleteTextViewWithDel;

    private static final String AUTO_FIELD_MAIL_NO = "mail_no";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.fragment_query, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoCompleteTextViewWithDel.updateAdapter(AUTO_FIELD_MAIL_NO);
    }

    private void initView(View view) {
        getActivity().setTitle("查询");

        spinner = (Spinner) view.findViewById(R.id.spinner);
        String[] expressSpinner = getResources().getStringArray(R.array.express_spinner);
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, expressSpinner);
        spinner.setAdapter(spinnerAdapter);

        autoCompleteTextViewWithDel = (AutoCompleteTextViewWithDel) view.findViewById(R.id.num_input);
        autoCompleteTextViewWithDel.initAutoCTV(AUTO_FIELD_MAIL_NO);

        querySubmit = (Button) view.findViewById(R.id.query_submit);
        querySubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query_submit:
                String num = "";
                num = autoCompleteTextViewWithDel.getText().toString().trim();
                if (!NetUtils.isNetAvailable(getContext())) {
                    Toast.makeText(getContext(), "无网络！", Toast.LENGTH_SHORT).show();
                } else if (num.equals("")) {
                    Toast.makeText(getContext(), "请输入单号！", Toast.LENGTH_SHORT).show();
                } else {
                    autoCompleteTextViewWithDel.saveAutoCTV(AUTO_FIELD_MAIL_NO);
                    Intent intent = new Intent(getContext(), ResultActivity.class);
                    intent.putExtra("num", num);
                    intent.putExtra("expressCom", getSpinnerChs((String) spinner.getSelectedItem()));
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private String getSpinnerChs(String string) {
        switch (string) {
            case "顺丰":
                return "shunfeng";
            case "申通":
                return "shentong";
            case "中通":
                return "zhongtong";
            case "圆通":
                return "yuantong";
            case "汇通":
                return "huitong";
            case "韵达":
                return "yunda";
            case "天天":
                return "tiantian";
            case "EMS":
                return "ems";
            default:
                return "none";
        }

    }
}
