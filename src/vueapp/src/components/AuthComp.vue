<template>
    <div class="page-container">
        <form novalidate class="md-layout " @submit.prevent="validateUser">
            <md-card class="md-layout-item">
                <md-card-header>
                    <div class="md-title">Welcome to Lingo</div>
                </md-card-header>

                <md-card-content>
                    <div class="md-layout-item">
                        <md-field>
                            <label>Username</label>
                            <md-input name="first-name" id="first-name" v-model="form.username"/>
                        </md-field>
                        <md-field>
                            <label>Password</label>
                            <md-input name="first-name" id="pass-word" v-model="form.password"/>
                        </md-field>
                        <md-field v-if="!isLoginForm">
                            <label>Password</label>
                            <md-input name="first-name" id="pass-word-check" v-model="form.passWordCheck"/>
                        </md-field>
                        <md-label style="color: darkred">{{errorMsg}}</md-label>
                    </div>
                </md-card-content>

                <md-progress-bar md-mode="indeterminate" v-if="sending" />

                <md-card-actions style="display:flex; justify-content: space-between">
                    <md-button type="submit" class="md-primary" @click="isLoginForm = !isLoginForm">{{(!isLoginForm) ? "Already registered?" : "Create a new account?"}}</md-button>
                    <md-button type="submit" class="md-primary" v-if="!isLoginForm" @click="userRegister">Register</md-button>
                    <md-button type="submit" class="md-primary" v-if="isLoginForm" @click="userLogin">Login</md-button>
                </md-card-actions>
            </md-card>
        </form>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: "AuthComp",
        data(){
            return {
                form: {},
                isLoginForm: true,
                errorMsg: ''
            }
        },
        methods: {
            validateUser(){
                this.errorMsg = '';
                try{
                    if (!this.form.password || !this.form.username){
                        this.errorMsg = "Please fill in a username and password"
                        return false;
                    }
                }catch (ignored) {console.log(ignored)}

                if (!this.isLoginForm){
                    try{
                        if (this.form.password.length < 5){
                            this.errorMsg = "Password not long enough"
                            return false;
                        }
                    }catch (ignored) {console.log(ignored)}

                    try{
                        if (this.form.password !== this.form.passWordCheck){
                            this.errorMsg = "Passwords don't match"
                            return false;
                        }
                    }catch (ignored) {console.log(ignored)}
                }
            },
            userRegister(){
                axios.post('http://localhost:8070/register', {
                    username: this.form.username,
                    password: this.form.password
                })
                    .then((res) => {
                        console.log(res)
                        this.isLoginForm = true;
                    })
                    .catch((err) => {
                        this.errorMsg = err.message
                    })
            },
            userLogin(){
                axios.post('http://localhost:8070/login', {
                    username: this.form.username,
                    password: this.form.password
                })
                    .then((res) => {
                        localStorage.setItem("auth", res.headers.authorization)
                        this.$router.push("/game")
                    })
                    .catch((err) => {
                        this.errorMsg = err.message
                    })
            }
        }
    }
</script>

<style scoped>
    .page-container{
        display: flex;
        justify-content: center;
        padding-top: 60px;
    }

    form{
        min-width: 350px;
    }
</style>