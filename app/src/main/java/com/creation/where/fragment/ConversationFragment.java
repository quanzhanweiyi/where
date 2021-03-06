package com.creation.where.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.creation.where.R;
import com.creation.where.activity.ChatMainActivity;
import com.creation.where.adapter.ConversationAdapter;
import com.creation.where.po.IMConversation;
import com.creation.where.po.MapChatMsg;
import com.creation.where.po.MapChatMsgEntity;

import java.util.ArrayList;
import java.util.List;

import static com.creation.where.util.DebugUtils.ShowErrorInf;

public class ConversationFragment extends Fragment {
	private TextView errorText;
    private final static int MSG_REFRESH = 2;
    protected EditText query;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<IMConversation> conversationList ;
	protected ListView conversationListView;
    protected FrameLayout errorItemContainer;

    protected boolean isConflict;
    private Handler handler;   //线程处理专用
    MapChatMsg mapChatMsg=null;  //信息传递

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fx_fragment_conversation, container, false);
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
        initView();
        setUpView();

        // 线程处理专用
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){

                }
                else if(msg.what==2){

                }
                else if (msg.what == -1)
                    ShowErrorInf("服务器请求异常");
                else if (msg.what == -2){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("mapChatMsg",mapChatMsg);
                    Intent intent=new Intent(getActivity(), ChatMainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        };
    }
	
	protected void initView() {
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
        View errorView = View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        errorItemContainer.setVisibility(View.GONE);

        conversationListView = (ListView) getView().findViewById(R.id.list);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread() {
                    public void run() {  //获取本地数据库的聊天内容（可以阅后即焚）
                        Looper.prepare();
                        IMConversation imConversation=conversationList.get(position);
                        boolean res=getMsgInfo(imConversation.getUserName());
                        if(res)              //可以成功获取内容
                            handler.sendEmptyMessage(2);
                        else
                            handler.sendEmptyMessage(-2);
                        Looper.loop();
                    };
                }.start();
            }
        });
    }

    //获取聊天信息
    public boolean getMsgInfo(String username){
        //获取未读信息
        List<MapChatMsgEntity> list=new ArrayList<>();

        mapChatMsg=new MapChatMsg();
        mapChatMsg.setChatwith(username);
        mapChatMsg.setMapMessage(list);
        return false;
    }
	
	protected void setUpView() {
	    loadConversationList();
	    ConversationAdapter adapter = new ConversationAdapter(getActivity(), conversationList);
	    conversationListView.setAdapter(adapter);
    }

    /**
     * 模拟加载数据
     */
    private void loadConversationList(){
        conversationList = new ArrayList<IMConversation>();
        conversationList.add(new IMConversation(R.drawable.user01,"Jobowen",1,"10:23","my love",false));
        conversationList.add(new IMConversation(R.drawable.user02,"李海兰",1,"10:23"," so what",false));
        conversationList.add(new IMConversation(R.drawable.user03,"刘洋",3,"10:23","no zuo no die",false));
        conversationList.add(new IMConversation(R.drawable.user04,"林芷昀",1,"10:23","哈哈哈",true));
        conversationList.add(new IMConversation(R.drawable.user05,"李伟杰",0,"10:23","你们真逗！",true));
        conversationList.add(new IMConversation(R.drawable.user06,"蔡嘉敏",0,"10:23","可不是嘛！",true));
    }
	
}
