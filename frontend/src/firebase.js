import {initializeApp} from "firebase/app";
import {getMessaging, getToken, onMessage} from "firebase/messaging";
import {sendTokenToServer} from "@/api";

const firebaseConfig = {
    apiKey: process.env.VUE_APP_FIREBASE_API_KEY,
    authDomain: process.env.VUE_APP_FIREBASE_AUTH_DOMAIN,
    projectId: process.env.VUE_APP_FIREBASE_PROJECT_ID,
    storageBucket: process.env.VUE_APP_FIREBASE_STORAGE_BUCKET,
    messagingSenderId: process.env.VUE_APP_FIREBASE_MESSAGING_SENDER_ID,
    appId: process.env.VUE_APP_FIREBASE_APP_ID,
    measurementId: process.env.VUE_APP_FIREBASE_MEASUREMENT_ID
};

let messaging = null;

if (firebaseConfig.projectId) {
    try {
        const app = initializeApp(firebaseConfig);
        messaging = getMessaging(app);
    } catch (e) {
        console.warn('Firebase 초기화 실패:', e.message);
    }
} else {
    console.warn('Firebase 설정이 없습니다. 푸시 알림이 비활성화됩니다.');
}

export const requestForToken = async () => {
    if (!messaging) {
        console.warn('Firebase가 초기화되지 않았습니다.');
        return;
    }

    try {
        const currentToken = await getToken(messaging, {
            vapidKey: process.env.VUE_APP_FIREBASE_VAPID_KEY,
        });
        if (currentToken) {
            await sendTokenToServer(currentToken);
        } else {
            console.log("토큰을 가져오지 못했습니다. 알림 권한을 허용하세요.");
        }
    } catch (error) {
        console.error("토큰을 가져오는 동안 오류 발생:", error);
    }
};

export const onMessageListener = () =>
    new Promise((resolve) => {
        if (!messaging) return;

        onMessage(messaging, (payload) => {
            console.log('메시지 수신:', payload);
            resolve(payload);

            const notificationTitle = payload.notification.title;
            const notificationOptions  = {
                body: payload.notification.body,
                icon: '/house.png',
            };
            new Notification(notificationTitle, notificationOptions);
        });
    });
