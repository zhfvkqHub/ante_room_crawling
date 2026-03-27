<template>
  <header class="header">
    <div class="header-content">
      <div class="left-section" @click="goHome">
        <div class="logo-wrapper" @mouseenter="showViewCount" @mouseleave="hideViewCount">
          <img src="@/assets/house.jpg" alt="Logo" class="logo" />
          <transition name="fade">
            <div v-if="isViewCountVisible" class="view-count-tooltip">
              {{ todayViews !== null ? `${todayViews.toLocaleString()}` : '...' }}
            </div>
          </transition>
        </div>
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
      </div>
    </div>

    <!-- 문의 사항 입력 폼 -->
    <div v-if="isContactFormOpen" class="contact-overlay" @click.self="toggleContactForm">
      <div class="contact-form">
        <button class="close-button" @click="toggleContactForm">닫기</button>
        <iframe src="https://docs.google.com/forms/d/e/1FAIpQLSekPNcYtEIuhzVtuDotw1_hf9FAemGBRLVSBMCyyl8SWbGVOw/viewform?embedded=true"
                width="100%"
                height="100%"
                frameborder="0"
                marginheight="0"
                marginwidth="0"
        >
          로드 중…
        </iframe>
      </div>
    </div>
  </header>
</template>

<script>
import {axiosGetTodayViews, axiosPostPageView} from '@/api';

export default {
  name: 'AppHeader',
  props: {
    title: {
      type: String,
      default: '청년안심주택 모집공고 수집'
    },
    navItems: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      isContactFormOpen: false,
      isViewCountVisible: false,
      todayViews: null,
    };
  },
  mounted() {
    axiosPostPageView().catch(() => {});
  },
  methods: {
    toggleContactForm() {
      this.isContactFormOpen = !this.isContactFormOpen;
    },
    goHome() {
      window.location.reload();
    },
    async showViewCount() {
      this.isViewCountVisible = true;
      try {
        const response = await axiosGetTodayViews();
        this.todayViews = response.data;
      } catch {
        this.todayViews = null;
      }
    },
    hideViewCount() {
      this.isViewCountVisible = false;
    }
  },
};
</script>

<style scoped>
.header {
  width: 100%;
  background-color: #1f577e;
  color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 8px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.left-section {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  margin-right: 14px;
}

.logo {
  height: 50px;
  border-radius: 35%;
}

.view-count-tooltip {
  position: absolute;
  top: calc(100% + 8px);
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: #ffd700;
  padding: 6px 12px;
  border-radius: 8px;
  font-size: 0.8rem;
  font-weight: 600;
  white-space: nowrap;
  pointer-events: none;
  z-index: 1001;
  letter-spacing: 0.5px;
}

.view-count-tooltip::before {
  content: '';
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  border: 6px solid transparent;
  border-bottom-color: rgba(0, 0, 0, 0.8);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.site-title {
  font-family: "Hi Melody", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 1.6rem;
  text-align: left;
  color: #ECF0F1;
  white-space: nowrap;
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

.contact-button {
  background-color: rgba(255, 255, 255, 0.15);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.contact-button:hover {
  background-color: rgba(255, 255, 255, 0.25);
}

.contact-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  z-index: 2000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.contact-form {
  position: relative;
  width: 640px;
  height: 680px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  overflow: hidden;
}

.close-button {
  background-color: #e74c3c;
  color: #fff;
  border: none;
  padding: 6px 14px;
  cursor: pointer;
  position: absolute;
  border-radius: 6px;
  top: 10px;
  right: 10px;
  z-index: 10;
  font-size: 0.85rem;
  transition: background-color 0.2s;
}

.close-button:hover {
  background-color: #c0392b;
}

@media (max-width: 768px) {
  .header-content {
    padding: 6px 12px;
  }

  .logo-wrapper {
    margin-right: 8px;
  }

  .logo {
    height: 36px;
  }

  .site-title {
    font-size: 1rem;
  }

  .contact-button {
    padding: 6px 12px;
    font-size: 0.8rem;
  }

  .contact-overlay {
    align-items: flex-end;
  }

  .contact-form {
    width: 100%;
    height: 85%;
    border-radius: 16px 16px 0 0;
  }

  .navigation {
    display: none;
  }
}
</style>
