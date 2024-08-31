<template>
  <div class="container">
    <Header />
    <main class="main-content">
      <NoticeList
          :notices="notices"
          :currentPage="currentPage"
          :pageSize="pageSize"
          :totalPages="totalPages"
          @prevPage="prevPage"
          @nextPage="nextPage"
      />
    </main>
  </div>
</template>

<script>
import Header from '@/components/common/Header.vue';
import NoticeList from '@/components/notice/NoticeList.vue';
import { axiosGetNotice } from '@/api';

export default {
  name: 'MainView',
  components: {
    Header,
    NoticeList
  },
  data() {
    return {
      notices: [],
      currentPage: 1,
      totalPages: 1,
      pageSize: 15
    }
  },
  mounted() {
    this.fetchNotices();
  },
  methods: {
    async fetchNotices() {
      const response = await axiosGetNotice(this.currentPage, this.pageSize);
      this.notices = response.data.content;
      this.totalPages = response.data.totalPages;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchNotices();
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchNotices();
      }
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0;
  background-color: #ffffff;
  font-family: 'Noto Sans KR', sans-serif;
}

.main-content {
  padding-top: 80px;
  padding-bottom: 100px;
  margin: 40px auto;
  background-color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

@media (max-width: 768px) {
  .main-content {
    padding: 10px;
    padding-top: 80px;
    padding-bottom: 100px;
  }
}
</style>
