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

import com.github.promeg.pinyinhelper.Pinyin;

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

        abcMap= createMap();
        adapter = new MyAdapter(this, list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
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


    private List<String> getList() {
        List<String> l = new ArrayList<>();
        Collections.addAll(l, stringCitys);
        return l;
    }
    private Map<String,Integer> createMap(){
        Map<String,Integer> map=new HashMap<>();

        List<String> l=getList();
        List<String> abcList=new ArrayList<>();
        for (int i = 0; i <l.size() ; i++) {
            abcList.add(transformPinYin(l.get(i)));
            String name=l.get(i);
            name=String.valueOf(abcList.get(i).charAt(0))+name;
            l.remove(i);
            l.add(i,name);
        }
       list.addAll(l);
        Collections.sort(list);
        for (int i = 0; i <list.size() ; i++) {
            String a=String.valueOf(list.get(i).charAt(0));
            if (!map.containsKey(a)){
                map.put(a,i);
            }
            String name=list.get(i);
            name=name.replace(a,"");
            list.remove(i);
            list.add(i,name);
        }

        return map;
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

    public String transformPinYin(String character) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < character.length(); i++) {
            buffer.append(Pinyin.toPinyin(character.charAt(i)));
        }
        return buffer.toString();
    }
}
