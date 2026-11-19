import { Instance, types } from 'mobx-state-tree';

/**
 * Payload shape for node data in the graph visualization.
 * Contains immutable metadata passed to cytoscape and stored as a frozen snapshot.
 */
export interface NodeDataPayload {
    id: string;
    label: string;
    color: string;
}

/**
 * Position object compatible with cytoscape layout API.
 * Frozen snapshot for immutable positioning data.
 */
export interface Position {
    x: number;
    y: number;
}

/**
 * Payload shape for edge data in the graph visualization.
 * Direction and label are optional metadata for styling and labeling edges.
 */
export interface EdgeDataPayload {
    id?: string;
    source?: string;
    target?: string;
    label?: string;
    direction?: 'DIRECTED' | 'UNDIRECTED';
}

export const NodeModel = types.model('NodeModel', {
    id: types.identifier,
    label: types.string,
    group: types.literal('nodes'),
    data: types.frozen<NodeDataPayload>(),
    position: types.maybe(types.frozen<Position>()),
    color: types.maybe(types.string),
});

export const EdgeModel = types.model('EdgeModel', {
    id: types.identifier,
    source: types.string,
    target: types.string,
    group: types.literal('edges'),
    data: types.frozen<EdgeDataPayload>(),
});

export const NeighborhoodExplorerStore = types
    .model('NeighborhoodExplorerStore', {
        ids: types.optional(types.array(types.string), []),
        nodes: types.optional(types.array(NodeModel), []),
        edges: types.optional(types.array(EdgeModel), []),
    })
    .views((self) => ({
        get elements() {
            return [...self.nodes, ...self.edges];
        },
    }))
    .actions((self) => ({
        setIds(value: string[]) {
            self.ids.replace(value);
        },

        addNodes(value: Array<{ data?: Partial<NodeDataPayload>; position?: Position | undefined }>) {
            const nextNodes = value.map((node) => {
                const data = node?.data ?? {};
                const id = String(data.id ?? '');

                return NodeModel.create({
                    id,
                    label: String(data.label ?? id),
                    group: 'nodes',
                    data: { id, label: data.label ?? id, color: data.color ?? '#000000' } as NodeDataPayload,
                    position: node?.position ?? undefined,
                    color: data?.color ?? undefined,
                });
            });

            self.nodes.replace([...self.nodes, ...nextNodes]);
            self.ids.replace(self.nodes.map((node) => node.id));
        },

        setGraphData(
            nodes: Array<{ data?: Partial<NodeDataPayload>; position?: Position }>,
            edges: Array<{ data?: Partial<EdgeDataPayload>; source?: string; target?: string }>,
        ) {
            const nextNodes = nodes.map((node) => {
                const data = node?.data ?? {};
                const id = String(data.id ?? '');

                return NodeModel.create({
                    id,
                    label: String(data.label ?? id),
                    group: 'nodes',
                    data: { id, label: data.label ?? id, color: data.color ?? '#000000' } as NodeDataPayload,
                    position: node?.position ?? undefined,
                    color: data?.color ?? undefined,
                });
            });

            const nextEdges = edges.map((edge) => {
                const data = edge?.data ?? {};
                const source = String(data.source ?? edge?.source ?? '');
                const target = String(data.target ?? edge?.target ?? '');
                const label = String(data.label ?? `${source}->${target}`);
                const fallbackId = `${source}::${target}::${label}`;
                const id = String(data.id ?? fallbackId);

                return EdgeModel.create({
                    id,
                    source,
                    target,
                    group: 'edges',
                    data: { id, source, target, label, direction: data.direction } as EdgeDataPayload,
                });
            });

            self.nodes.replace(nextNodes);
            self.edges.replace(nextEdges);
            self.ids.replace(self.nodes.map((node) => node.id));
        },

        setFromStringifiedElements(value: string) {
            const parsed: unknown = JSON.parse(value);
            const elements = Array.isArray(parsed) ? parsed : [];
            const nodes = elements.filter(
                (item: unknown): item is Record<string, unknown> => item != null && typeof item === 'object' && 'group' in item && (item as Record<string, unknown>).group === 'nodes',
            );
            const edges = elements.filter(
                (item: unknown): item is Record<string, unknown> => item != null && typeof item === 'object' && 'group' in item && (item as Record<string, unknown>).group === 'edges',
            );

            const nextNodes = nodes.map((node) => {
                const data = (node.data as Partial<NodeDataPayload>) ?? {};
                const id = String(data.id ?? '');

                return NodeModel.create({
                    id,
                    label: String(data.label ?? id),
                    group: 'nodes',
                    data: { id, label: data.label ?? id, color: data.color ?? '#000000' } as NodeDataPayload,
                    position: (node.position as Position) ?? undefined,
                    color: data?.color ?? undefined,
                });
            });

            const nextEdges = edges.map((edge) => {
                const data = (edge.data as Partial<EdgeDataPayload>) ?? {};
                const source = String(data.source ?? (edge.source as string) ?? '');
                const target = String(data.target ?? (edge.target as string) ?? '');
                const label = String(data.label ?? `${source}->${target}`);
                const fallbackId = `${source}::${target}::${label}`;
                const id = String(data.id ?? fallbackId);

                return EdgeModel.create({
                    id,
                    source,
                    target,
                    group: 'edges',
                    data: { id, source, target, label, direction: data.direction } as EdgeDataPayload,
                });
            });

            self.nodes.replace(nextNodes);
            self.edges.replace(nextEdges);
            self.ids.replace(self.nodes.map((node) => node.id));
        },

        clear() {
            self.ids.replace([]);
            self.nodes.replace([]);
            self.edges.replace([]);
        },
    }));

export type INeighborhoodExplorerStore = Instance<typeof NeighborhoodExplorerStore>;
