<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-left">
<!--        <p>문의사항: <a href="mailto:hamoey@naver.com">hamoey@naver.com</a></p>-->
      </div>
      <div class="footer-right">
      <p>월-금 8:00~19:00 30분 간격으로 크롤링 됩니다.</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter'
}
</script>

<style scoped>
.footer {
  width: 100%;
  background-color: #1f577e;
  color: white;
  padding: 20px 0;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  position: relative;
  bottom: 0;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-left, .footer-right {
  font-size: 1rem;
}

.footer a {
  color: #1ABC9C;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.footer a:hover {
  color: #ECF0F1;
}
</style>
