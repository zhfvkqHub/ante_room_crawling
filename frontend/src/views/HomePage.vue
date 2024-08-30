<template>
  <div class="container">
    <h1 class="title">공지사항 목록</h1>
    <table class="notice-table">
      <thead>
      <tr>
        <th width="10%">번호</th>
        <th width="55%">제목</th>
        <th width="20%">게시일</th>
        <th width="15%">사이트</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="notice in notices" :key="notice.id" :class="{'highlight-today': isToday(notice.publishedDate)}">
        <td>{{ notice.id }}</td>
        <td>{{ notice.title }}</td>
        <td>{{ notice.publishedDate }}</td>
        <td><a :href="notice.siteUrl" target="_blank" class="notice-link">보기</a></td>
      </tr>
      </tbody>
    </table>
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">이전</button>
      <span>페이지 {{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">다음</button>
    </div>
  </div>
</template>

<script>
import {axiosGetNotice} from "@/api";
import {format} from 'date-fns';

export default {
  data() {
    return {
      notices: [],
      currentPage: 1,
      totalPages: 1,
      pageSize: 10
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
    isToday(dateString) {
      const today = format(new Date(), 'yyyy-MM-dd');
      return dateString === today;
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
  margin: 40px auto;
  padding: 20px;
  background-color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  font-family: 'Noto Sans KR', sans-serif;
}

.title {
  text-align: center;
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 30px;
  font-weight: bold;
}

.notice-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #f9f9f9;
  border-radius: 8px;
  overflow: hidden;
}

.notice-table th, .notice-table td {
  padding: 15px 20px;
  text-align: left;
  font-size: 1rem;
  border-bottom: 1px solid #ddd;
}

.notice-table thead {
  background-color: #007BFF;
  color: white;
  font-weight: bold;
  text-transform: uppercase;
}

.notice-table tbody tr:nth-child(even) {
  background-color: #f4f4f4;
}

.notice-table tbody tr:hover {
  background-color: #e2e6ea;
}

.notice-link {
  color: #007BFF;
  text-decoration: none;
  font-weight: bold;
}

.notice-link:hover {
  text-decoration: underline;
}

.highlight-today {
  background-color: #FFD700;
  color: #333;
  font-weight: bold;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.2);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

.pagination button {
  margin: 0 10px;
  padding: 10px 15px;
  font-size: 1rem;
  cursor: pointer;
  border: 1px solid #007BFF;
  background-color: white;
  color: #007BFF;
  border-radius: 4px;
  transition: background-color 0.3s, color 0.3s;
}

.pagination button:hover:not(:disabled) {
  background-color: #007BFF;
  color: white;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.pagination span {
  margin: 0 15px;
  font-size: 1.2rem;
  color: #555;
}

@media (max-width: 768px) {
  .container {
    padding: 10px;
  }

  .title {
    font-size: 2rem;
  }

  .notice-table th, .notice-table td {
    font-size: 0.875rem;
  }

  .pagination button {
    padding: 8px 14px;
    font-size: 0.875rem;
  }

  .pagination span {
    font-size: 1rem;
  }
}
</style>
