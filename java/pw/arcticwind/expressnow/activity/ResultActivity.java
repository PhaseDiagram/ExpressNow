package pw.arcticwind.expressnow.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.WeakReference;

import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.model.DividerItemDecoration;
import pw.arcticwind.expressnow.model.ExpressInfo;
import pw.arcticwind.expressnow.model.ExpressStepAdapter;
import pw.arcticwind.expressnow.utils.HttpUtils;
import pw.arcticwind.expressnow.utils.ParseJSON;

//查询详情页
//带 header 的 RecyclerView; 使用 Handler 进行线程间通信; SwipeRefreshLayout; 对已查询过的结果进行缓存, 并在新查询回应前显示缓存
public class ResultActivity extends AppCompatActivity implements View.OnClickListener, HttpUtils.HttpCallbackListener, SwipeRefreshLayout.OnRefreshListener {
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ExpressStepAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private HttpHandler httpHandler = new HttpHandler(this);
    private ToastHandler toastHandler = new ToastHandler(this);

    private boolean favorite = false;

    //这里不用 0, 因为 int 的默认值就是 0, 不注意时会引起混乱
    //虽然手动赋 -1, 或者使用中注意点可以避免, 不过还是这样省事
    private static final int FILE_CREATE_FAIL = 1;
    private static final int FILE_IO_EXCEPTION = 2;
    private static final int FILE_NOT_FOUND = 3;
    private static final int FILE_NULL_JSON = 4;
    private static final int HTTP_ERROR = 5;

    //jsonObject只在readCache和queryFromServer的onHttpFinish回调中被赋值；expressCom，num只在onCreate被赋值
    //为了fabOnClick的回调方便还是写在这里了。其余地方都传参，便于后续将方法移到其他类等
    private JSONObject jsonObject;
    private String expressCom;
    private String num;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        expressCom = getIntent().getStringExtra("expressCom");
        num = getIntent().getStringExtra("num");
        favorite = isFavorite(expressCom, num);
        initView();
        readCache(expressCom, num);
        HttpUtils.queryFromServer(expressCom, num, this);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(ResultActivity.this);
        progressDialog.setMessage("处理中...");
        progressDialog.show();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_result);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.list_result);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        View headerView = LayoutInflater.from(this).inflate(R.layout.cell_header_result, recyclerView, false);
        adapter = new ExpressStepAdapter();
        adapter.setHeaderView(headerView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (favorite) {
            fab.setImageResource(R.drawable.ic_clear_black_24dp);
        }
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (favorite) {
                    removeFromFavor(expressCom, num);
                } else {
                    saveToFavor(jsonObject, expressCom, num);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onHttpFinish(String response) {
        try {
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message message = Message.obtain();
        message.obj = jsonObject;
        httpHandler.sendMessage(message);
    }

    @Override
    public void onHttpError(Exception e) {
        Message message = Message.obtain();
        message.what = HTTP_ERROR;
        toastHandler.sendMessage(message);
    }

    @Override
    public void onRefresh() {
        HttpUtils.queryFromServer(expressCom, num, this);
    }

    private static class HttpHandler extends Handler {
        private final WeakReference<ResultActivity> activityWeakReference;

        public HttpHandler(ResultActivity activity) {
            activityWeakReference = new WeakReference<ResultActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            activityWeakReference.get().updateView((JSONObject) msg.obj, false);
            if (activityWeakReference.get().progressDialog.isShowing()) {
                activityWeakReference.get().progressDialog.dismiss();
            }
            if (activityWeakReference.get().swipeRefreshLayout.isRefreshing()) {
                activityWeakReference.get().swipeRefreshLayout.setRefreshing(false);
            }
        }
    };
    private static class ToastHandler extends Handler {
        private final WeakReference<ResultActivity> activityWeakReference;

        public ToastHandler(ResultActivity activity) {
            activityWeakReference = new WeakReference<ResultActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FILE_NULL_JSON:
                    Toast.makeText(activityWeakReference.get(), "FILE_NULL_JSON", Toast.LENGTH_SHORT).show();
                    break;
                case FILE_CREATE_FAIL:
                    Toast.makeText(activityWeakReference.get(), "FILE_CREATE_FAIL", Toast.LENGTH_SHORT).show();
                    break;
                case FILE_IO_EXCEPTION:
                    Toast.makeText(activityWeakReference.get(), "FILE_IO_EXCEPTION", Toast.LENGTH_SHORT).show();
                    break;
                case FILE_NOT_FOUND:
                    Toast.makeText(activityWeakReference.get(), "FILE_NOT_FOUND", Toast.LENGTH_SHORT).show();
                    break;
                case HTTP_ERROR:
                    Toast.makeText(activityWeakReference.get(), "HTTP_ERROR", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            if (activityWeakReference.get().progressDialog.isShowing()) {
                activityWeakReference.get().progressDialog.dismiss();
            }
            if (activityWeakReference.get().swipeRefreshLayout.isRefreshing()) {
                activityWeakReference.get().swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    private boolean isFavorite(String com, String num) {
        return findFavorPath(com, num).exists();
    }

    private File findCachePath(String com, String num) {
        return new File(getFilesDir().toString() + File.separator + "history" + File.separator + com + num);
    }

    private File findFavorPath(String com, String num) {
        return new File(getFilesDir().toString() + File.separator + "favor" + File.separator + com + num);
    }

    private void saveToCache(JSONObject jsonObject, String com, String num) {
        File file = findCachePath(com, num);
        save(jsonObject, file);
    }

    private void saveToFavor(JSONObject jsonObject, String com, String num) {
        File file = findFavorPath(com, num);
        save(jsonObject, file);

        Toast.makeText(this, "收藏成功！", Toast.LENGTH_SHORT).show();
        fab.setImageResource(R.drawable.ic_clear_black_24dp);

        favorite = true;
    }

    private void save(JSONObject jsonObject, File file) {
        Message message = Message.obtain();
        if (jsonObject == null) {
            message.what = FILE_NULL_JSON;
            toastHandler.sendMessage(message);
            return;
        }
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    message.what = FILE_CREATE_FAIL;
                }
            } catch (IOException e) {
                e.printStackTrace();
                message.what = FILE_IO_EXCEPTION;
            }
        }

        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(file));
            out.print(jsonObject.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            message.what = FILE_NOT_FOUND;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        toastHandler.sendMessage(message);
    }

    private void removeFromFavor(String com, String num) {
        File file = findFavorPath(com, num);
        if (!file.delete()) {
            Toast.makeText(this, "删除收藏失败！", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "取消收藏！", Toast.LENGTH_SHORT).show();
        fab.setImageResource(R.drawable.ic_add_black_24dp);

        favorite = false;
    }

    private void readCache(String com, String num) {
        File file = findCachePath(com, num);
        if (file.exists()) {
            jsonObject = readJSONFile(file);
            updateView(jsonObject, true);
        }
    }

    private JSONObject readJSONFile(File file) {
        try {
            String temp;
            StringBuilder jsonString = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
                jsonString.append(temp);
            }
            return new JSONObject(jsonString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateView(JSONObject jsonObject, boolean fromCache) {
        View headerView = adapter.getHeaderView();
        TextView statusView = (TextView) headerView.findViewById(R.id.status_result_right);
        TextView comView = (TextView) headerView.findViewById(R.id.com_result_right);
        TextView numView = (TextView) headerView.findViewById(R.id.num_result_right);
        TextView telView = (TextView) headerView.findViewById(R.id.tel_result_right);
        String error = ParseJSON.preParse(jsonObject);
        statusView.setText(error);
        if (!error.equals("查询中...")) {
            if (!fromCache) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        } else {
            ExpressInfo expressInfo = ParseJSON.parseJSON(jsonObject);
            // TODO: 2016/3/3 异常处理
            statusView.setText(expressInfo.getStatus());
            comView.setText(expressInfo.getCom());
            numView.setText(expressInfo.getNum());
            telView.setText(expressInfo.getTel());
            adapter.modData(expressInfo.getExpressStepList());
            recyclerView.setAdapter(adapter);
            if (!fromCache) {
                saveToCache(jsonObject, expressInfo.getComSpell(), expressInfo.getNum());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}