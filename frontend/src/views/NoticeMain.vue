<template>
  <div class="container">
    <Header />
    <main class="main-content">
      <NoticeList />
    </main>
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/common/Header.vue';
import Footer from '@/components/common/Footer.vue';
import NoticeList from '@/components/notice/NoticeList.vue';

export default {
  name: 'MainView',
  components: {
    Header,
    Footer,
    NoticeList
  }
}
</script>

<style scoped>
.container {
  font-family: "Jua", sans-serif;
  font-weight: 400;
  font-style: normal;
}

.main-content {
  padding-top: 100px;
  padding-bottom: 50px;
  max-width: 1080px;
  margin: 0 auto;
}
</style>
