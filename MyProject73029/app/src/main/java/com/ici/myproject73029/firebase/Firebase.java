package com.ici.myproject73029.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.ici.myproject73029.Constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Firebase {

    public FirebaseFirestore startFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
        return db;
    }

    public void updateItems(final FirebaseFirestore db) {
        db.collectionGroup("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = new HashMap<>();
                        if (document.get("create_date") == null) {
                            map.put("create_date", Timestamp.now());
                        }
                        document.getReference().set(map, SetOptions.merge());
                    }
                }
            }
        });
    }

    public void addData(FirebaseFirestore db) {
        Map<String, Object> exhibition = new HashMap<>();
        exhibition.put("title", "툴루즈 로트렉 展");
        exhibition.put("description", "[알립니다] * 코로나바이러스감염증-19 확산을 방지하고자 미술관을 찾으시는 관람객들께서는 반드시 마스크 " +
                "착용을 요청 드립니다. 마스크 미착용시 전시장 입장을 제한하오니 적극 협조 부탁 드립니다. 더불어, 예술의전당은 관람객 안전을 위해 시설내 방역과 손소독제 등 위생용품을 상시 배치하고, 일부 출입문을 폐쇄하여 제한 개방하고 있습니다. 관람객 여러분께서도 코로나바이러스감염증-19의 확산 방지를 위해 필수적으로 국민행동을 숙지하여 내방하여 주시기를 당부 드립니다.      * 수도권대상 강화된 방역조치가 무기한 연장됨에 따라, 6월 16일부터 강화된 사회적 거리두기 방침에 준하여 도슨트를 운영합니다. 각 운영시간별로 현장에서 순번표를 배부하여 50명 이내 인원 제한을 두고 운영합니다. 마스크 필수 착용 및 1m 거리유지에 협조 부탁드립니다. 인원초과 시 도슨트 시작 후 10분간 입장이 제한될 예정이니 관람에 참고해주시기 바랍니다. 본 전시는 코로나바이러스로부터 관람객분들의 안전에 최선을 다하겠습니다                     [특별할인] * 예매할인     - 일반 13,500원 - 청소년 10,200원 - 어린이 8,000원         * 앵콜기념 사전예매 할인(사용기간: 티켓 예매 다음날부터 ~ 7월 14일까지) - 1차 70% (판매종료)              - 2차 50% (판매종료)        - 3차 30% (판매기간: 6월 11일 ~ 6월 25일)  일반 10,500원  청소년 8,400원  어린이 7,000원     * 재관람할인 (판매기간: ~ 7월 5일까지) - 입장가 1,000원  (현장에서 구매 가능 / 중복할인 불가 / 툴루즈 로트렉 展(2020.01.14 ~ 2020.05.16) 관람티켓 실물지참 시 할인 적용)       * 문화가 있는 날 - 매월 마지막 수요일은 문화가 있는 날! (6/24, 7/29, 8/26)    - 기본가에서 50% 할인 (중복할인 불가, 현장매표소에서만 할인 가능) - 할인적용 기간 : 행사 당일 오후 6시 - 8시 (현장매표소에서 티켓 구매 시 적용가능) - 야간연장개관 진행 : 오전 11시 - 오후 9시 (입장마감 오후 8시)          [도슨트 운영] 11시, 13시, 15시, 17시 (4회) * 각 운영시간별로 50명 이내 인원제한을 두며 현장에서 순번표를 배부할 예정입니다.  인원 초과 시 도슨트 시작 후 10분간 입장이 제한될 예정이니 관람에 참고 해주시기 바랍니다. 마스크 필수 착용 및 1m 이상 거리유지에 관람객 분들의 협조 부탁드립니다           [전시소개]  이번 전시는 올해 1월14일부터 5월 16일까지 진행됐던 툴루즈로트렉 단독전의 앵콜전시로, 코로나감염증바이러스-19(이하 코로나-19)상황으로 인해 다음 순회전시 예정이었던 미국 플로리다에서의 전시가 취소되고, 원작소장 박물관 소재지인 그리스로의 반출도 어려운 상황속에서 코로나-19를 성곡적으로 극복하고 있는 대한민국에서의 재개관을 제안받아 앵콜전시를 진행하게 되었다. 코로나 ?19의 여파로 관람이 어려웠던 관객들에게 다시 한 번 관람기회를 제공한다.  그리스 아테네에 위치한 헤라클레이돈 미술관 (Herakleidon Museum)이 소장하고 있는 드로잉, 판화, 스케치 등 150여점의 진품작품이 전시되며, 이번 앵콜전시에서는 미디어아트섹션을 보강하여 툴루즈로트렉의 유화작품 8점을 선별하여 미디어아트로 재현하여 전시할 예정이다. 이전 전시에서 호응이 좋았던 마지막 섹션의 영상에도 추가내용 및 비하인드 스토리를 추가하여 감동을 더했고, 전시장의 디자인 컨셉도 시즌감을 반영하여 이전 전시보다 업그레이드 된 색다른 즐거움을 줄 예정이다.   [작가소개] 툴루즈로트렉은 주로 활동했던 프랑스 파리나 19세기말의 시대를 넘어 세계의 모든 사람들에게 사랑받는 작가로 37년 이라는 짧은 생애동안 5,000여점의 작품을 남기며 몽마르트의 작은 거인으로 많은 사람들에게 감동을 주는 작가이다.   ");
        exhibition.put("creator", "예술의전당");

        db.collection("All").document("툴루즈 로트렉 展").set(exhibition)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constant.TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constant.TAG, "Error writing document", e);
                    }
                });
    }
}
