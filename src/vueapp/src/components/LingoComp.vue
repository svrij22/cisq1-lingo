<template>
    <div class="game-root">
        <div class="score-box" :style="{'width': 35*(wordLength) + 'px', 'min-width': '100px'}" >
            <h2>Score</h2> <h2>{{this.game.score}}</h2>
        </div>
        <div v-if="state" class="game-box">
            <div class="row-box" v-for="(row, indx) in game.guesses" :key="'row' + indx">
                <div :class="'letter-box ' + letterStyling(row, x-1)" v-for="x in game.wordToGuess.length" :key="x">{{guessLetter(row, x-1)}}</div>
            </div>
            <div class="row-box" v-if="state == 'STARTED'">
                <div class="letter-box" v-for="x in game.wordToGuess.length" :key="x">{{inputLetter(x-1)}}</div>
            </div>
            <div class="input-box">
                <input v-model="inputStr" class="input" v-if="state == 'STARTED'"/>
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
            },
            wordLength(){
                try{
                    return this.game.wordToGuess.length;
                }catch (e) {
                    return 0;
                }
            }
        },
        methods: {
            newGame(){
                axios.get(`http://localhost:8070/game/new`)
                .then(res => {
                    this.game = res.data;
                    this.inputStr = this.game.wordToGuess.value[0];
                })
            },
            resetGame(){
                axios.get(`http://localhost:8070/game/reset?id=${this.game.id}`)
                    .then(res => {
                        this.game = res.data;
                        this.inputStr = this.game.wordToGuess.value[0];
                    })
            },
            findCorrectLetter(pos){
                try{
                    let letter = '.';
                    _.each(this.game.guesses, guess => {
                        let map = guess.letters[pos];
                        if (map.status === 'CORRECT') letter = map.character;
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
                    let resMap = row.letters[pos];
                    return resMap.character;
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
                    let resMap = row.letters[pos];
                    if (resMap.status === 'CORRECT') return 'correct-letter';
                    if (resMap.status === 'NEAR') return 'near-letter';
                }catch (e) {
                    console.log(e)
                }
            }
        },
        watch: {
            inputStr(){
                try{
                    if (this.inputStr === "") this.inputStr = this.game.wordToGuess.value[0];

                    if (this.inputStr.length === this.game.wordToGuess.length){
                        this.postGuess()
                    }

                    if (this.inputStr.length > this.game.wordToGuess.length){
                        this.inputStr = this.inputStr.slice(0, this.game.wordToGuess.length)
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
        flex-direction: column;
        padding: 20px;
        margin-top: 100px;
    }

    .row-box{
        align-self: end;
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

    .game-box{
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    .input-box{
        width: 100%;
    }

    .score-box{
        display: flex;
        width: 100%;
        justify-content: space-between;
    }
</style>