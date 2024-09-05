<template>
  <div>
    <div class="search-bar">
      <input
          v-model="searchKeyword"
          @keyup.enter="fetchNotices"
          type="text"
          placeholder="제목 검색"
          class="search-input"
      />
      <select v-model="selectedSiteName" @change="fetchNotices" class="filter-select">
        <option value="">전체 사이트</option>
        <option v-for="site in siteOptions" :key="site.value" :value="site.value">
          {{ site.name }}
        </option>
      </select>
      <select v-model="selectedConstituency" @change="fetchNotices" class="filter-select">
        <option value="">전체 지역구</option>
        <option v-for="constituency in constituencyOptions" :key="constituency.value" :value="constituency.value">
          {{ constituency.name }}
        </option>
      </select>
      <button @click="fetchNotices" class="search-button">Search</button>
    </div>

    <div class="last-crawled-time">
      마지막 업데이트 완료 시간: {{ lastCrawledTime || '불러오는 중...' }}
    </div>

    <table class="notice-table">
      <thead>
      <tr>
        <th width="10%">번호</th>
        <th width="15%">사이트명</th>
        <th width="50%">제목</th>
        <th width="15%">게시일</th>
        <th width="10%">사이트</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(notice, index) in notices" :key="notice.id" :class="{'highlight-today': isToday(notice.publishedDate)}">
        <td>{{ index + 1 + (currentPage - 1) * pageSize }}</td>
        <td>{{ notice.siteName }}</td>
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
import {format} from 'date-fns';
import {axiosGetLastCrawlingTime, axiosGetNotice} from '@/api';

export default {
  data() {
    return {
      notices: [],
      currentPage: 1,
      totalPages: 1,
      pageSize: 15,
      searchKeyword: '',
      selectedSiteName: '',
      selectedConstituency: '',
      lastCrawledTime: '',
      siteOptions: [
        { name: "서초꽃마을주얼리", value: "SEOCHEO_FLOWER_VILLAGE_JEWELRY" },
        { name: "제이스타상봉", value: "J_STAR_SANGBONG" },
        { name: "BX201서울대", value: "BX201_SEOUL_NATIONAL_UNIVERSITY" },
        { name: "동대문역사문화공원", value: "DONGDAEMUN_HISTORY_CULTURE_PARK" },
        { name: "도림브라보", value: "DORIM_BRAVO" },
        { name: "더클래식동작", value: "THE_CLASSIC_DONGJAK" },
        { name: "신대방삼거리역골든노블레스", value: "DONGJAK_GOLDEN_NOBLESS" },
        { name: "엘리스", value: "ELLICE" },
        { name: "포레나당산", value: "FORENA_DANGSAN" },
        { name: "잠실센트럴파크", value: "JAMSIL_CENTRAL_PARK" },
        { name: "청년안심주택", value: "YOUTH_SAFE_HOUSE" }
      ],
      constituencyOptions: [
        { name: "강남구", value: "GANGNAM" },
        { name: "강동구", value: "GANGDONG" },
        { name: "강북구", value: "GANGBUK" },
        { name: "강서구", value: "GANGSEO" },
        { name: "관악구", value: "GWANAK" },
        { name: "광진구", value: "GWANGJIN" },
        { name: "구로구", value: "GURO" },
        { name: "금천구", value: "GEUMCHEON" },
        { name: "노원구", value: "NOWON" },
        { name: "도봉구", value: "DOBONG" },
        { name: "동대문구", value: "DONGDAEMUN" },
        { name: "동작구", value: "DONGJAK" },
        { name: "마포구", value: "MAPO" },
        { name: "서대문구", value: "SEODAEMUN" },
        { name: "서초구", value: "SEOCHO" },
        { name: "성동구", value: "SEONGDONG" },
        { name: "성북구", value: "SEONGBUK" },
        { name: "송파구", value: "SONGPA" },
        { name: "양천구", value: "YANGCHEON" },
        { name: "영등포구", value: "YEONGDEUNGPO" },
        { name: "용산구", value: "YONGSAN" },
        { name: "은평구", value: "EUNPYEONG" },
        { name: "종로구", value: "JONGNO" },
        { name: "중구", value: "JUNG" },
        { name: "중랑구", value: "JUNGNANG" },
        { name: "기타", value: "ETC" }
      ]

    }
  },
  mounted() {
    this.fetchNotices();
    this.fetchLastCrawledTime();
  },
  methods: {
    async fetchNotices() {
      const params = {
        page: this.currentPage - 1,
        size: this.pageSize,
        siteName: this.selectedSiteName || null,
        constituency: this.selectedConstituency || null,
        searchType: 'TITLE',
        searchKeyword: this.searchKeyword || null
      };

      const response = await axiosGetNotice(params);
      this.notices = response.data.content;
      this.totalPages = response.data.totalPages;
    },
    async fetchLastCrawledTime() {
      const response = await axiosGetLastCrawlingTime();
      this.lastCrawledTime = this.formatCrawlingTime(response.data.lastCrawlingTime);
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
    },
    isToday(dateString) {
      const today = format(new Date(), 'yyyy-MM-dd');
      return dateString === today;
    },
    formatCrawlingTime(time) {
      if (!time) return null;
      return format(new Date(time), 'yyyy년 MM월 dd일 HH:mm');
    }
  }
}
</script>

<style scoped>
.notice-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #ffffff;
  border-radius: 8px;
  overflow: hidden;
}

.notice-table th {
  background-color: #2980B9;
  color: #ECF0F1;
  font-weight: bold;
  text-transform: uppercase;
  padding: 15px 20px;
  text-align: left;
}

.notice-table td {
  padding: 15px 20px;
  text-align: left;
  font-size: 1rem;
  border-bottom: 1px solid #ddd;
}

.notice-table tbody tr:nth-child(even) {
  background-color: #f7f7f7;
}

.notice-table tbody tr:nth-child(odd) {
  background-color: #ffffff;
}

.notice-table tbody tr:hover {
  background-color: #f0f0f0;
}

.notice-link {
  color: #2980B9;
  text-decoration: none;
  font-weight: bold;
}

.notice-link:hover {
  text-decoration: underline;
  color: #1A5276;
}

.highlight-today {
  background-color: #F39C12 !important;
  color: #ffffff;
  font-weight: bold;
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
  border: 1px solid #2980B9;
  background-color: white;
  color: #2980B9;
  border-radius: 4px;
  transition: background-color 0.3s, color 0.3s;
}

.pagination button:hover:not(:disabled) {
  background-color: #2980B9;
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

.search-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.search-input {
  flex-grow: 1;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filter-select {
  padding: 10px;
  margin-left: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-button {
  margin-left: 10px;
  padding: 10px 15px;
  font-size: 1rem;
  background-color: #2980B9;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #1A5276;
}

.last-crawled-time {
  margin-bottom: 15px;
  font-size: 1.2rem;
  color: #333;
}
</style>
