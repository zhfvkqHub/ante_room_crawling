<template>
  <div class="container">
    <h1 class="title">Notice List</h1>
    <table class="notice-table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Site URL</th>
        <th>Title</th>
        <th>Published Date</th>
        <th>Link</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="notice in notices" :key="notice.id" :class="{'highlight-today': isToday(notice.publishedDate)}">
        <td>{{ notice.id }}</td>
        <td>{{ notice.siteUrl }}</td>
        <td>{{ notice.title }}</td>
        <td>{{ notice.publishedDate }}</td>
        <td><a :href="notice.link" target="_blank" class="notice-link">View Notice</a></td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import {axiosGetNotice} from "@/api";
import {format} from 'date-fns';

export default {
  data() {
    return {
      notices: []
    }
  },
  mounted() {
    this.fetchNotices()
  },
  methods: {
    async fetchNotices() {
      const response = await axiosGetNotice();
      this.notices = response.data;
    },
    isToday(dateString) {
      const today = format(new Date(), 'yyyy-MM-dd');
      return dateString === today;
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
  background-color: #f9f9f9;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.title {
  text-align: center;
  font-size: 2rem;
  color: #333;
  margin-bottom: 20px;
}

.notice-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
}

.notice-table th, .notice-table td {
  padding: 12px 15px;
  text-align: left;
  font-size: 1rem;
}

.notice-table thead {
  background-color: #007BFF;
  color: white;
  font-weight: bold;
}

.notice-table tbody tr:nth-child(even) {
  background-color: #f2f2f2;
}

.notice-table tbody tr:hover {
  background-color: #e9ecef;
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
  background-color: cornflowerblue;
  font-weight: bold;
}

@media (max-width: 768px) {
  .container {
    padding: 10px;
  }

  .title {
    font-size: 1.5rem;
  }

  .notice-table th, .notice-table td {
    font-size: 0.875rem;
  }
}
</style>
