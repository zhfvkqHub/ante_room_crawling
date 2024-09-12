<template>
  <div class="container">
    <Header />

    <!-- 카카오 애드센스 광고 영역 -->
<!--    <div class="ads-container">-->
<!--      <ins class="kakao_ad_area" style="display:none;"-->
<!--           data-ad-unit="DAN-vGxhtmQFpbiRdNfb"-->
<!--           data-ad-width="728"-->
<!--           data-ad-height="90"></ins>-->
<!--    </div>-->

    <main class="main-content">
      <NoticeList />
    </main>

    <!-- 카카오 애드센스 광고 영역 -->
<!--    <div class="ads-container-footer">-->
<!--      <ins class="kakao_ad_area" style="display:none;"-->
<!--           data-ad-unit="DAN-cSgq62orTBdhY5z6"-->
<!--           data-ad-width="320"-->
<!--           data-ad-height="50"></ins>-->
<!--    </div>-->

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

.ads-container {
  margin-top: 90px;
  text-align: center;
}

.ads-container-footer {
  margin-bottom: 10px;
  text-align: center;
}
</style>
