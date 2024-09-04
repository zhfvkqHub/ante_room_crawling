<template>
  <div class="container">
    <Header />
    <main class="main-content">
      <NoticeList />
    </main>
  </div>
</template>

<script>
import Header from '@/components/common/Header.vue';
import NoticeList from '@/components/notice/NoticeList.vue';

export default {
  name: 'MainView',
  components: {
    Header,
    NoticeList
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
</style>
