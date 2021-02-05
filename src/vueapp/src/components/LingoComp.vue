<template>
    <div class="game-root">
        <div v-if="state">
            <div class="row-box" v-for="(row, indx) in game.guesses" :key="'row' + indx">
                <div :class="'letter-box ' + letterStyling(row, x)" v-for="x in game.word.length" :key="x">{{guessLetter(row, x-1)}}</div>
            </div>
            <div class="row-box" v-if="state == 'STARTED'">
                <div class="letter-box" v-for="x in game.word.length" :key="x">{{inputLetter(x-1)}}</div>
            </div>
            <div :style="{'width': 35*(game.word.length) + 'px'}">
                <input v-model="inputStr" :style="{'width': 35*(game.word.length) + 'px'}" class="input" v-if="state == 'STARTED'"/>
                <button class="input" v-if="state != 'STARTED'" @click="resetGame"> Reset </button>
            </div>
        </div>
        <div v-else>
            <button @click="newGame"> New Game </button>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';
    import _ from 'lodash';

    export default {
        name: "LingoComp",
        data(){
            return {
                game: {},
                inputStr: ''
            }
        },
        computed: {
            state() {
                return this.game?.state;
            }
        },
        methods: {
            newGame(){
                axios.get(`http://localhost:8070/game/new`)
                .then(res => {
                    this.game = res.data;
                    this.inputStr = this.game.word.value[0];
                })
            },
            resetGame(){
                axios.get(`http://localhost:8070/game/reset?id=${this.game.id}`)
                    .then(res => {
                        this.game = res.data;
                        this.inputStr = this.game.word.value[0];
                    })
            },
            findCorrectLetter(pos){
                try{
                    let letter = '.';
                    _.each(this.game.guesses, guess => {
                        let map = guess.resultMap[pos];
                        if (map[1] === 'CORRECT') letter = map[0];
                    })
                    return letter;
                }catch (e) {
                    return ',';
                }
            },
            inputLetter(pos){
                let str = this.inputStr[pos]
                if (!str) {
                    str = this.findCorrectLetter(pos)
                }
                return str
            },
            guessLetter(row, pos){
                try{
                    let resMap = row.resultMap[pos];
                    return resMap[0];
                }catch (e) {
                    console.log(e)
                }
            },
            postGuess(){
                axios.post(`http://localhost:8070/game/guess?id=${this.game.id}&guess=${this.inputStr}`)
                    .then(res => {
                        this.game = res.data;
                        this.inputStr = '';
                    })
            },
            letterStyling(row, pos){
                try{
                    let resMap = row.resultMap[pos-1];
                    if (resMap[1] === 'CORRECT') return 'correct-letter';
                    if (resMap[1] === 'NEAR') return 'near-letter';
                }catch (e) {
                    console.log(e)
                }
            }
        },
        watch: {
            inputStr(){
                try{
                    if (this.inputStr === "") this.inputStr = this.game.word.value[0];

                    if (this.inputStr.length === this.game.word.length){
                        this.postGuess()
                    }

                    if (this.inputStr.length > this.game.word.length){
                        this.inputStr = this.inputStr.slice(0, this.game.word.length)
                    }
                    this.inputStr = this.inputStr.replace(/[0-9]/g, '');
                }catch (e) {
                    console.log(e.message)
                }
            }
        }
    }
</script>

<style scoped>
    .game-root{
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .row-box{
        display: flex;
    }

    .letter-box{
        width: 32px;
        height: 32px;
        background-color: #8080d0;
        color: white;
        border: 2px solid black;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
        text-transform: capitalize;
    }

    .correct-letter{
        background-color: #d55757;
        border: 2px solid #800c0c;
    }

    .near-letter{
        background-color: #f8dc32;
        border: 2px solid #80670c;
    }

    .input{
        border: 2px solid gray;
        margin-top: 10px;
        width: 100%;
    }
</style>