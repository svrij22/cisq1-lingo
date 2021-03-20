<template>
    <div class="container">
        <md-toolbar class="toolbar">

            <!--Navigation switch-->
            <md-button class="md-icon-button" @click="showNavigation = true">
                <md-icon>menu</md-icon>
            </md-button>

            <img src="../assets/logo.png" style="width: 180px">
        </md-toolbar>

        <md-drawer :md-active.sync="showNavigation" md-swipeable>
            <md-toolbar class="md-transparent" md-elevation="0">
                <span class="md-title">My App name</span>
            </md-toolbar>

            <md-list>
                <md-list-item @click="$router.push('/game')">
                    <md-icon>move_to_inbox</md-icon>
                    <span class="md-list-item-text">Game</span>
                </md-list-item>

                <md-list-item>
                    <md-icon>send</md-icon>
                    <span class="md-list-item-text">Leaderboard</span>
                </md-list-item>

                <md-list-item @click="logOut">
                    <md-icon>error</md-icon>
                    <span class="md-list-item-text">Uitloggen</span>
                </md-list-item>
            </md-list>
        </md-drawer>

        <router-view/>
    </div>
</template>

<script>

    export default {
        name: "AppFrame",
        data: () => ({
            showNavigation: false
        }),
        methods: {
            logOut(){
                localStorage.removeItem("auth");
                this.$router.push("/auth")
            }
        }
    }
</script>

<style lang="scss" scoped>
    .toolbar{
        background-color: aliceblue;
    }

    .container{
        height: 100%;
    }

    .md-drawer {
        width: 230px;
        max-width: calc(100vw - 125px);
    }

    .md-content {
        padding: 16px;
    }
</style>
