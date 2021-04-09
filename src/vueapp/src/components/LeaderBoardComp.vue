<template>
    <div class="container">
        <div class="card-holder">
            <md-card >
                <md-table>
                    <md-table-row>
                        <md-table-head>Name</md-table-head>
                        <md-table-head md-numeric>Score</md-table-head>
                    </md-table-row>

                    <md-table-row v-for="user in leaderboardData" v-bind:key="user.username">
                        <md-table-cell >{{user.username}}</md-table-cell>
                        <md-table-cell md-numeric>{{user.score}}</md-table-cell>
                    </md-table-row>
                </md-table>
            </md-card>
        </div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "LeaderBoardComp",
        data() {
            return {
                leaderboardData : {}
            }
        },
        mounted() {
            axios.get('http://localhost:8070/users/leaderboard')
                .then((res) => {
                    this.leaderboardData = res.data;
                })
        }
    }
</script>

<style scoped>
    .container{
        display: flex;
        justify-content: center;
    }
    .card-holder{
        width: 100%;
        max-width: 500px;
        text-align: left;
        padding: 10px;
    }
</style>