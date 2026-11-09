#!/usr/bin/perl -w
#
# use JSON::MaybeXS;
# use Text::CSV qw( csv );

use HTTP::Tiny;
use URI::Escape;


$baseURL = 'https://n1h.org/api/';
@endpoints = ( 
    {'param' => 'smiles', 'endpoint' => 'compounds/by-smiles?value=%s' },
    {'param' => 'smiles', 'endpoint' => 'compounds/by-substructure?smiles=%s&take=20&page=0' },
    {'param' => 'none', 'endpoint' => 'entity-type/all' },
    {'param' => 'permute', 'endpoint' => 'search?query=%s' },
    {'param' => 'term', 'endpoint' => 'search?query=%s' }
);

@terms = (
    'pain',
    'malaria',
    'methylbut-2-en-1-yl',
    'pentanoic',
    'morphine',
    'allium',
    'chrom%25acet',
    'phyllum',
    'tetra%25hex',
    '15-11-22-16-7-3-1',
    'acidosis',
    'vulgaris',
    'hordeum',
    'triti',
    'strepto',
    'folia',
    'spectab',
    'rhizo',
    'iculata',
    'thall',
    'leuco',
    'cepha',
    'capit',
    'landia',
    'angust',
    'diabetes');

@smiles = (
    'CC0CCCCC(C)CCC=CCCC',
    'CCC(=O)OCCCN',
    'c1cc(O)ccc1CC=CC',
    'CC1=CC(=O)OC1CO',
    'CCSCCC1CCCCC1',
    'Cc1ncc(N(O)O)n1CCO',
    'COc1ccc2c3c(n(CCCc4nc(C)no4)c(=O)c2c1OC)-c1ccccc1C3=O',
    'CC(=O)OC(CC(C)C)CC(C)c1ccc(C)cc1O',
    'CC(=O)NCC(c1ccccc1)C(=O)',
    'CC(=O)OC(COC(=O)CCCCCCCCCCCCCCCCC)COP(=O)(O)OC'
    );

@fragments = (
    "acet",
    "acid",
    "amin",
    "benzo",
    "brom",
    "but",
    "chlor",
    "chrom",
    "enoic",
    "fur",
    "hex",
    "hydroxy",
    "meth",
    "napht",
    "oct",
    "pent",
    "phen",
    "piper",
    "prop",
    "pyr",
    "spiro",
    "tetra",
    "yl",
);


sub doGet {
    my $url = shift;
    my $http = HTTP::Tiny->new(
        timeout => 10,
        agent => 'Perl/HTTP::Tiny',
    );
    my $res = $http->get(
        $url,
        {
            headers => {
                'Accept' => '*/*',
            }
        }
    );
    if ( $res->{success} ) {
        print(".");
    } else {
        printf("Failure:  status=%s, url=%s, content=%s\n",
            $res->{status}, $url, $res->{content});
        exit(1);
    }
}

sub doPost {
    my $url = shift;
    my $payload = shift;

    my $http = HTTP::Tiny->new(
        timeout => 10,
        agent => 'Perl/HTTP::Tiny',
    );

    my $res = $http->post(
        $url,
        {
            headers => {
                'Content-Type' => 'application/vnd.api+json',
                'Accept' => '*/*',
            },
            content => encode_json($payload),
        }
    );

    if ( $res->{success} ) {
        print(".");
    } else {
        printf("Failure:  status=%s, url=%s, content=%s\n", 
            $res->{status}, $url, $res->{content});
        exit(1);
    }
}

sub setSmiles {
    my $i = int(rand(@smiles));
    return sprintf(shift, uri_escape($smiles[$i]));
}

sub setTerm {
    my $i = int(rand(@terms));
    return sprintf(shift, uri_escape($terms[$i]));
}

sub setPermuteFragments {
    my $query = "";
    my $sep = "";
    for (my $i = 0; $i < 3; $i++) {
        my $j = int(rand(@fragments));
        $query .= $sep . $fragments[$j];
        $sep = "%25";
    }
    return sprintf(shift, uri_escape($query));
}
    
    

sub doRequest {
    my $endpoint = shift;

    $url = $baseURL . $endpoint->{'endpoint'};
    if ($endpoint->{'param'} eq 'smiles') {
        $url = &setSmiles($url);
        &doGet($url);
    }
    if ($endpoint->{'param'} eq 'term') {
        $url = &setTerm($url);
        &doGet($url);
    }
    if ($endpoint->{'param'} eq 'permute') {
        $url = &setPermuteFragments($url);
        &doGet($url);
    }
    if ($endpoint->{'param'} eq 'none') {
        &doGet($url);
    }
}

my @counts;
my $round = 0;

while (1 == 1) {
    $round++;
    my $i = int(rand(@endpoints));
    $counts[$i]++;
    $endpoint = $endpoints[$i];
    doRequest($endpoint);
    if (($round % 100) == 0) {
        print("\n");
        for($i = 0; $i < @endpoints; $i++) {
            printf("%d --> %d\n", $i, $counts[$i]);
        }
    }
}

