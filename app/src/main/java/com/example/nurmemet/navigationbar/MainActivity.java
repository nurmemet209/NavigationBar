package com.example.nurmemet.navigationbar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NavigationBar navigationBar;
    private MyAdapter adapter;
    private List<String> list = new ArrayList<>();
    private Map<String, Integer> abcMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        navigationBar = (NavigationBar) findViewById(R.id.navigation_view);
        list.add("nurmememt");
        list.add("nurmememt");
        list.add("nurmememt");
        list.add("nurmememt");
        list.add("nurmememt");
        list.add("alim");
        list.add("alim");
        list.add("alim");
        list.add("alim");
        list.add("alim");
        list.add("alim");
        list.add("alim");
        list.add("samat");
        list.add("samat");
        list.add("samat");
        list.add("samat");
        list.add("pahirdin");
        list.add("pahirdin");
        list.add("pahirdin");
        list.add("pahirdin");
        list.add("pahirdin");
        list.add("pahirdin");
        list.add("erqal");
        list.add("erqal");
        list.add("erqal");
        list.add("erqal");
        list.add("erqal");
        list.add("qarluq");
        list.add("qarluq");
        list.add("qarluq");
        list.add("qarluq");
        list.add("qarluq");

//        abcMap.put("a", 5);
//        abcMap.put("n", 0);
//        abcMap.put("s", 12);
//        abcMap.put("p", 16);
//        abcMap.put("e", 22);
//        abcMap.put("q", 27);


        abcMap.put("A", 5);
        abcMap.put("B", 5);
        abcMap.put("C", 5);
        abcMap.put("D", 5);
        abcMap.put("E", 22);
        abcMap.put("F", 5);
        abcMap.put("G", 5);
        abcMap.put("H", 5);
        abcMap.put("I", 5);
        abcMap.put("J", 5);
        abcMap.put("K", 5);
        abcMap.put("L", 5);
        abcMap.put("M", 5);
        abcMap.put("N", 0);
        abcMap.put("O", 5);
        abcMap.put("P", 16);
        abcMap.put("Q", 27);
        abcMap.put("R", 27);
        abcMap.put("S", 12);
        abcMap.put("T", 12);
        abcMap.put("U", 12);
        abcMap.put("V", 12);
        abcMap.put("W", 12);
        abcMap.put("X", 12);
        abcMap.put("Y", 12);
        abcMap.put("Z", 12);



        adapter = new MyAdapter(this, list);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        navigationBar.setRecycleView(recyclerView);
        navigationBar.setAbcMap(abcMap);


    }


    private class MyAdapter extends RecyclerView.Adapter {

        private List<String> list;
        private Context context;

        public MyAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
            RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
            };
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView tv = (TextView) holder.itemView.findViewById(R.id.title);
            tv.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }
    }



    public static String[] stringCitys = new String[]{
            "合肥", "张家界", "宿州", "淮北", "阜阳", "蚌埠", "淮南", "滁州",
            "马鞍山", "芜湖", "铜陵", "安庆", "安阳", "黄山", "六安", "巢湖",
            "池州", "宣城", "亳州", "明光", "天长", "桐城", "宁国",
            "徐州", "连云港", "宿迁", "淮安", "盐城", "扬州", "泰州",
            "南通", "镇江", "常州", "无锡", "苏州", "江阴", "宜兴",
            "邳州", "新沂", "金坛", "溧阳", "常熟", "张家港", "太仓",
            "昆山", "吴江", "如皋", "通州", "海门", "启东", "大丰",
            "东台", "高邮", "仪征", "江都", "扬中", "句容", "丹阳",
            "兴化", "姜堰", "泰兴", "靖江", "福州", "南平", "三明",
            "复兴", "高领", "共兴", "柯家寨", "匹克", "匹夫", "旗舰", "启航",
            "如阳", "如果", "科比", "韦德", "诺维斯基", "麦迪", "乔丹", "姚明"
    };
}
