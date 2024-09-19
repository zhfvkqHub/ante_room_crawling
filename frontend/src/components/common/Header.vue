<template>
  <header class="header">
    <div class="header-content">
      <div class="left-section">
        <img src="@/assets/house.jpg" alt="Logo" class="logo" />
        <h1 class="site-title">{{ title }}</h1>
      </div>
      <nav class="navigation">
        <ul>
          <li v-for="(item, index) in navItems" :key="index">
            <a @click="executeMethod(item.method)">{{ item.name }}</a>
          </li>
        </ul>
      </nav>

      <div class="button-container">
        <button class="contact-button" @click="toggleContactForm">
          문의
        </button>
        <button class="push-button" @click="requestPush">
          푸시 요청
        </button>
      </div>
    </div>

    <!-- 문의 사항 입력 폼 -->
    <div class="contact-form" v-if="isContactFormOpen">
      <button class="close-button" @click="toggleContactForm">닫기</button>
      <iframe src="https://docs.google.com/forms/d/e/1FAIpQLSekPNcYtEIuhzVtuDotw1_hf9FAemGBRLVSBMCyyl8SWbGVOw/viewform?embedded=true"
              width="640"
              height="650"
              frameborder="0"
              marginheight="0"
              marginwidth="0"
      >
        로드 중…
      </iframe>
    </div>
  </header>
</template>

<script>
import {requestForToken} from '@/firebase';
import {openModal} from "@/api/common/modal";

export default {
  name: 'AppHeader',
  props: {
    title: {
      type: String,
      default: '청년안심주택 모집공고 수집'
    },
    navItems: {
      type: Array,
      default: () => [
        // Nav items here, if needed
      ]
    }
  },
  data() {
    return {
      isContactFormOpen: false,
    };
  },
  methods: {
    toggleContactForm() {
      this.isContactFormOpen = !this.isContactFormOpen;
    },
    requestPush() {
      requestForToken()
          .then(() => {
            openModal('알림', '푸시 요청이 완료되었습니다.');
          })
          .catch(error => {
            console.error(error);
            openModal('오류', '푸시 요청 중 오류가 발생했습니다.');
          });
    },
  },
};
</script>

<style scoped>
.header {
  width: 100%;
  background-color: #1f577e;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 5px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.left-section {
  display: flex;
  align-items: center;
}

.logo {
  height: 60px;
  margin-right: 20px;
  border-radius: 35%;
}

.site-title {
  font-family: "Hi Melody", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 1.8rem;
  text-align: left;
  color: #ECF0F1;
}

.navigation ul {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
}

.navigation li {
  margin-left: 15px;
}

.navigation a {
  color: #ECF0F1;
  text-decoration: none;
  font-weight: 500;
  font-size: 1rem;
  transition: color 0.3s ease;
}

.navigation a:hover {
  color: #1ABC9C;
}

.button-container {
  display: flex;
  align-items: center;
}

.push-button {
  background-color: #8e44ad;
  color: white;
  border: none;
  padding: 10px 10px;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.push-button:hover {
  background-color: #5e3370;
}

.contact-button {
  background-color: #2980b9;
  color: white;
  border: none;
  padding: 10px 10px;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-right: 10px;
}

.contact-button:hover {
  background-color: #164564;
}

.contact-form {
  position: fixed;
  top: 10%;
  right: 20px;
  width: 670px;
  height: 680px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  z-index: 2000;
}

.close-button {
  background-color: #0e8b34;
  color: #fff;
  border: none;
  padding: 7px 12px;
  cursor: pointer;
  position: absolute;
  border-radius: 5px;
  top: 11px;
  right: 7px;
}

.close-button:hover {
  background-color: #0a5f24;
}

@media (max-width: 768px) {
  .contact-form {
    top: 5%;
    right: 5%;
    width: 90%;
    height: 80%;
    padding: 15px;
    box-sizing: border-box;
  }

  .contact-form iframe {
    width: 100%;
    height: calc(100% - 40px);
  }

  .close-button {
    top: 10px;
    right: 10px;
  }

  .header-content {
    align-items: center;
  }

  .logo {
    height: 50px;
  }

  .site-title {
    font-size: 1.5rem;
    text-align: center;
    margin: 10px 0;
  }

  .navigation ul {
    flex-direction: column;
    width: 100%;
  }

  .navigation li {
    margin: 5px 0;
  }
}
</style>
