<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="geraetTyp">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.geraetTyp.detail.title')">GeraetTyp</span> {{geraetTyp.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.geraetTyp.bezeichnung')">Bezeichnung</span>
                    </dt>
                    <dd>
                        <span>{{geraetTyp.bezeichnung}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.geraetTyp.gueltigBis')">Gueltig Bis</span>
                    </dt>
                    <dd>
                        <span>{{geraetTyp.gueltigBis}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.geraetTyp.technologie')">Technologie</span>
                    </dt>
                    <dd>
                        <span v-text="$t('gdb3App.Technologie.' + geraetTyp.technologie)">{{geraetTyp.technologie}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="geraetTyp.id" :to="{name: 'GeraetTypEdit', params: {geraetTypId: geraetTyp.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./geraet-typ-details.component.ts">
</script>
