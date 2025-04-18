<template>
  <div id="Header">
    <div id="Logo">
      <div id="LogoContent">
        <router-link to="/"><img src="@/assets/images/logo-topbar.gif"></router-link>
      </div>
    </div>

    <div id="Menu">
      <div id="MenuContent">
        <router-link to="/cart"><img align="middle" name="img_cart" src="@/assets/images/cart.gif" /></router-link>
        <img align="middle" src="@/assets/images/separator.gif" />
        
        <template v-if="!isLoggedIn">
          <router-link to="/userauth">Sign In</router-link>
          <img align="middle" src="@/assets/images/separator.gif" />
        </template>
        
        <template v-else>
          <a href="#" @click.prevent="logout">Sign Out</a>
          <img align="middle" src="@/assets/images/separator.gif" />
          <router-link to="/account">My Account</router-link>
          <img align="middle" src="@/assets/images/separator.gif" />
        </template>
        
        <router-link to="/help">?</router-link>
      </div>
    </div>

    <div id="Search">
      <div id="SearchContent">
        <form @submit.prevent="search">
          <input type="text" v-model="keyword" size="14" id="keyword">
          <input type="submit" value="Search">
        </form>
        <div id="productAutoComplete">
          <ul id="productAutoList">
            <!-- 自动完成列表将通过v-for动态生成 -->
          </ul>
        </div>
      </div>
    </div>

    <div id="QuickLinks">
      <router-link to="/category/FISH"><img src="@/assets/images/sm_fish.gif" /></router-link>
      <img src="@/assets/images/separator.gif" />
      <router-link to="/category/DOGS"><img src="@/assets/images/sm_dogs.gif" /></router-link>
      <img src="@/assets/images/separator.gif" />
      <router-link to="/category/REPTILES"><img src="@/assets/images/sm_reptiles.gif" /></router-link>
      <img src="@/assets/images/separator.gif" />
      <router-link to="/category/CATS"><img src="@/assets/images/sm_cats.gif" /></router-link>
      <img src="@/assets/images/separator.gif" />
      <router-link to="/category/BIRDS"><img src="@/assets/images/sm_birds.gif" /></router-link>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isLoggedIn: false,
      keyword: ''
    }
  },
  methods: {
    logout() {
      // 登出逻辑
      this.isLoggedIn = false
      this.$router.push('/')
    },
    search() {
      // 搜索逻辑
      this.$router.push({ path: '/search', query: { keyword: this.keyword } })
    }
  }
}
</script>

<style scoped>
#Header {
  background-color: black;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  position: relative;
  z-index: 1;
}

#LogoContent {
  margin: 0;
}

#LogoContent img {
  border: 0;
  height: 40px;
  width: auto;
}

#MenuContent {
  display: flex;
  align-items: center;
  gap: 10px;
}

#MenuContent a {
  color: yellow;
  font-weight: bold;
  text-decoration: none;
  transition: color 0.3s ease;
}

#MenuContent a:hover {
  color: #f40;
}

#MenuContent img[name="img_cart"] {
  margin-right: 0;
}

#Search {
  position: static;
  width: auto;
}

#SearchContent {
  display: flex;
  gap: 5px;
}

#SearchContent input[type="text"] {
  width: 140px;
  padding: 8px;
  border: 1px solid #ccc;
  background-color: gray;
  color: white;
  border-radius: 4px;
}

#SearchContent input[type="submit"] {
  padding: 8px 15px;
  background-color: #e0e0e0;
  color: black;
  border: none;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

#SearchContent input[type="submit"]:hover {
  background-color: #ccc;
}

#QuickLinks {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  width: auto;
}

#QuickLinks img {
  border: 0;
  vertical-align: middle;
  transition: transform 0.3s ease;
}

#QuickLinks img:hover {
  transform: scale(1.1);
}

#productAutoComplete {
  position: absolute;
  background: white;
  border: 1px solid #ccc;
  width: 180px;
  z-index: 100;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

#productAutoList {
  list-style: none;
  padding: 0;
  margin: 0;
}

#productAutoList li {
  padding: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

#productAutoList li:hover {
  background-color: #f5f5f5;
}
</style>