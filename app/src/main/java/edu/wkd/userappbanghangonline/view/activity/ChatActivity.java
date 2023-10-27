package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.ActivityChatBinding;
import edu.wkd.userappbanghangonline.model.obj.ChatMessage;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.ultil.Utils;
import edu.wkd.userappbanghangonline.view.adapter.ChatAdapter;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private FirebaseFirestore db;
    private List<ChatMessage> messageList;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initControl();
        listenMess();
        insertUser();
    }

    private void insertUser() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("id", UserUltil.user.getId());
        user.put("name", UserUltil.user.getUsername());
        db.collection(Utils.PATH_USERS).document(String.valueOf(UserUltil.user.getId())).set(user);
    }

    private void initControl() {
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessToFire();
            }
        });
    }

    private void sendMessToFire() {
        String strMess = binding.edMessage.getText().toString().trim();
        if(TextUtils.isEmpty(strMess)) {

        } else {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Utils.SEND_ID, String.valueOf(UserUltil.user.getId()));
            message.put(Utils.RECEIVE_ID, "1");
            message.put(Utils.MESS, strMess);
            message.put(Utils.DATETIME, new Date());
            message.put(Utils.AVATAR, UserUltil.user.getAvatar());
            db.collection(Utils.PATH_CHAT).add(message);
            binding.edMessage.setText("");
        }
    }

    private void listenMess() {
        // Lắng nghe sự kiện nhận dữ liệu
        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SEND_ID, String.valueOf(UserUltil.user.getId()))
                .whereEqualTo(Utils.RECEIVE_ID, "1")
                .addSnapshotListener(eventListener);

        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SEND_ID, "1")
                .whereEqualTo(Utils.RECEIVE_ID, String.valueOf(UserUltil.user.getId()))
                .addSnapshotListener(eventListener);
    }

    private EventListener<QuerySnapshot> eventListener =  new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if(error != null) {
                Log.d("zzz", "onEvent: " + error);
                Toast.makeText(ChatActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            if(value != null) {
                int count = messageList.size();
                for (DocumentChange documentChange: value.getDocumentChanges()) {
                    if(documentChange.getType() == DocumentChange.Type.ADDED) {
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.sendId = documentChange.getDocument().getString(Utils.SEND_ID);
                        chatMessage.receivedid = documentChange.getDocument().getString(Utils.RECEIVE_ID);
                        chatMessage.mess = documentChange.getDocument().getString(Utils.MESS);
                        chatMessage.avatar = documentChange.getDocument().getString(Utils.AVATAR);
                        chatMessage.dataObj = documentChange.getDocument().getDate(Utils.DATETIME);
                        chatMessage.datatime = formatDate(documentChange.getDocument().getDate(Utils.DATETIME));
                        messageList.add(chatMessage);
                    }
                }

                Collections.sort(messageList, (obj1, obj2) -> obj1.dataObj.compareTo(obj2.dataObj));
                if(count == 0) {
                    chatAdapter.notifyDataSetChanged();
                } else {
                    chatAdapter.notifyItemRangeInserted(messageList.size(), messageList.size());
                    binding.rcvChat.smoothScrollToPosition(messageList.size() - 1);
                }
            }
        }
    };

    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-hh:mm a", Locale.getDefault()).format(date);
    }

    private void initView() {
        messageList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rcvChat.setLayoutManager(linearLayoutManager);
        binding.rcvChat.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(this, messageList);
        binding.rcvChat.setAdapter(chatAdapter);
    }
}